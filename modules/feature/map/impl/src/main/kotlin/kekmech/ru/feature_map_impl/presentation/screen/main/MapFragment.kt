package kekmech.ru.feature_map_impl.presentation.screen.main

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.ErrorStateAdapterItem
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.ext_android.doOnApplyWindowInsets
import kekmech.ru.ext_android.dpToPx
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.views.setMargins
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_impl.DeeplinkHelper
import kekmech.ru.feature_map_impl.R
import kekmech.ru.feature_map_impl.databinding.FragmentMapBinding
import kekmech.ru.feature_map_impl.di.MapDependencies
import kekmech.ru.feature_map_impl.presentation.items.FilterTabItem
import kekmech.ru.feature_map_impl.presentation.items.MapMarkerAdapterItem
import kekmech.ru.feature_map_impl.presentation.items.TabBarAdapterItem
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.FilterTab
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEffect
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapStoreFactory
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.MapAppearance
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.animateCameraTo
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.applyAppearance
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.copyright
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.init
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.toMarkerType
import kekmech.ru.feature_map_impl.presentation.screen.main.view.BottomSheetBackgroundDrawable
import kekmech.ru.feature_map_impl.presentation.screen.main.view.ControlledScrollingLayoutManager
import kekmech.ru.feature_map_impl.presentation.screen.main.view.MarkersBitmapFactory
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_navigation.features.ScrollToTop
import kekmech.ru.lib_navigation.features.TabScreenStateSaver
import kekmech.ru.lib_navigation.features.TabScreenStateSaverImpl
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay
import kekmech.ru.coreui.R as CoreUiR
import kekmech.ru.res_strings.R.string as Strings

@Suppress("TooManyFunctions")
internal class MapFragment : Fragment(R.layout.fragment_map),
    ElmRendererDelegate<MapEffect, MapState>,
    ScrollToTop,
    TabScreenStateSaver by TabScreenStateSaverImpl("map") {

    private val dependencies by inject<MapDependencies>()
    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("Map")
    private val markersBitmapFactory: MarkersBitmapFactory by inject()
    private val viewBinding by viewBinding(FragmentMapBinding::bind)
    private val bottomSheetBackground by fastLazy {
        BottomSheetBackgroundDrawable(
            backgroundColor = requireContext().getThemeColor(CoreUiR.attr.colorWhite),
            topCornerRadius = resources.dpToPx(DEFAULT_CORNER_RADIUS).toFloat()
        )
    }
    private val appSettingsRepository by inject<AppSettingsRepository>()

    private var currentMarkers: List<Marker> = emptyList()
    private var appliedAppearance: MapAppearance? = null
    private var labelsOverlay: TilesOverlay? = null
    private var renderedTab: FilterTab? = null
    private var renderedMarkers: List<MapMarker>? = null
    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    private val store by androidElmStore(
        viewModelStoreOwner = { requireActivity() },
        savedStateRegistryOwner = { requireActivity() },
    ) { inject<MapStoreFactory>().value.create() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createMap()

        viewBinding.recyclerView.layoutManager = ControlledScrollingLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.background = bottomSheetBackground

        createBottomSheet(view)
    }

    override fun onResume() {
        super.onResume()
        viewBinding.mapView?.onResume()
        // the appearance can be changed in the settings while the map is in the background
        showAppearance(MapAppearance.fromCode(appSettingsRepository.getAppSettings().mapAppearanceType))
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            showAppearance(MapAppearance.fromCode(appSettingsRepository.getAppSettings().mapAppearanceType))
        }
    }

    override fun onPause() {
        viewBinding.mapView?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        bottomSheetCallback?.let {
            BottomSheetBehavior.from(viewBinding.recyclerView).removeBottomSheetCallback(it)
            bottomSheetCallback = null
        }
        viewBinding.recyclerView.adapter = null
        viewBinding.mapView?.let { map ->
            labelsOverlay?.let { map.overlays.remove(it) }
            labelsOverlay = null
            appliedAppearance = null
            currentMarkers.forEach { map.overlays.remove(it) }
            map.overlays.clear()
            map.onDetach()
        }
        currentMarkers = emptyList()
        renderedTab = null
        renderedMarkers = null
        super.onDestroyView()
    }

    private fun createBottomSheet(view: View) {
        view.doOnApplyWindowInsets { _, insets, padding ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            viewBinding.coordinatorLayout.setMargins(top = systemBarsInsets.top + padding.top)
            viewBinding.mapOverlayControls.setMargins(top = systemBarsInsets.top + padding.top)
        }
        val callback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    analytics.sendScroll("MapBottomSheet")
                }
                store.accept(MapEvent.Ui.Action.BottomSheetStateChanged(newState))
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                viewBinding.viewFade.alpha = slideOffset * MAX_OVERLAY_ALPHA
            }
        }
        bottomSheetCallback = callback
        BottomSheetBehavior.from(viewBinding.recyclerView).addBottomSheetCallback(callback)
    }

    private fun createMap() {
        val mapView = viewBinding.mapView ?: return
        mapView.init(requireContext())

        val appearance = MapAppearance.fromCode(appSettingsRepository.getAppSettings().mapAppearanceType)
        showAppearance(appearance)
        viewBinding.mapAppearanceToggle.check(appearance.buttonId())
        viewBinding.mapAppearanceToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            val selected = if (checkedId == R.id.mapAppearanceHybrid) MapAppearance.HYBRID else MapAppearance.SCHEME
            analytics.sendClick("MapAppearance_${selected.code}")
            appSettingsRepository.updateAppSettings { copy(mapAppearanceType = selected.code) }
            showAppearance(selected)
        }
    }

    private fun MapAppearance.buttonId() =
        if (this == MapAppearance.HYBRID) R.id.mapAppearanceHybrid else R.id.mapAppearanceScheme

    /** Changes the layer of the map (and the toggle) if it is not the one that is already shown */
    private fun showAppearance(appearance: MapAppearance) {
        val binding = viewBinding
        val mapView = binding.mapView ?: return
        if (appearance == appliedAppearance) return
        appliedAppearance = appearance
        labelsOverlay = mapView.applyAppearance(appearance, labelsOverlay)
        binding.mapAppearanceToggle.check(appearance.buttonId())
        binding.mapCopyright.text = appearance.copyright()
        binding.mapCopyright.isVisible = appearance.copyright() != null
    }

    override fun render(state: MapState) {
        adapter.update(MapListConverter().map(state))
        val behavior = BottomSheetBehavior.from(viewBinding.recyclerView)
        (viewBinding.recyclerView.layoutManager as ControlledScrollingLayoutManager)
            .isScrollingEnabled = behavior.state != BottomSheetBehavior.STATE_COLLAPSED

        if (state.bottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBackground.animate()
                .radius(0f)
                .start()
        } else {
            bottomSheetBackground.animate()
                .radius(1f)
                .start()
        }

        if (renderedTab != state.selectedTab || renderedMarkers != state.markers || (currentMarkers.isEmpty() && state.markers.isNotEmpty())) {
            renderedTab = state.selectedTab
            renderedMarkers = state.markers
            currentMarkers = generateMapMarkers(
                map = viewBinding.mapView,
                markers = state.markers,
                mapMarkers = currentMarkers,
                selectedTab = state.selectedTab,
            )
        }

        DeeplinkHelper.handleDeeplinkIfNecessary(dependencies.deeplinkDelegate, state, store)
    }

    override fun handleEffect(effect: MapEffect) =
        when (effect) {
            is MapEffect.AnimateCameraToPlace -> {
                viewBinding.mapView?.let { map ->
                    currentMarkers.find { it.title == effect.mapMarker.name }?.let { marker ->
                        map.animateCameraTo(marker)
                        marker.showInfoWindow()
                    }
                }
                if (effect.collapseBottomSheet) {
                    BottomSheetBehavior.from(viewBinding.recyclerView)
                        .state = BottomSheetBehavior.STATE_COLLAPSED
                    viewBinding.recyclerView.scrollToPosition(0)
                }
                Unit
            }

            is MapEffect.ShowLoadingError ->
                showBanner(Strings.map_loading_error_message)
        }

    private fun generateMapMarkers(
        map: MapView?,
        markers: List<MapMarker>?,
        mapMarkers: List<Marker>,
        selectedTab: FilterTab,
    ): List<Marker> {
        if (markers.isNullOrEmpty() || map == null) return emptyList()
        mapMarkers.forEach { map.overlays.remove(it) }

        val newMarkers = markers
            .filter { it.type == selectedTab.toMarkerType() }
            .map { markerData ->
                Marker(map).apply {
                    title = markerData.name
                    snippet = markerData.address
                    subDescription = markerData.tag
                    position = GeoPoint(markerData.location.lat, markerData.location.lng)
                    icon = BitmapDrawable(resources, markersBitmapFactory.getBitmap(markerData))
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    setOnMarkerClickListener { _, _ ->
                        showInfoWindow()
                        true
                    }
                }
            }
        map.overlays.addAll(newMarkers)
        map.invalidate()
        return newMarkers
    }

    override fun onScrollToTop() {
        store.accept(MapEvent.Ui.Action.ScrollToTop)
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            BottomSheetBehavior.from(viewBinding.recyclerView)
                .state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun createAdapter() = BaseAdapter(
        PullAdapterItem(),
        TabBarAdapterItem(
            tabs = createTabs(),
            onClickListener = {
                analytics.sendClick("Tab_$it")
                store.accept(MapEvent.Ui.Action.SelectTab(it))
            }
        ),
        SectionHeaderAdapterItem(),
        SpaceAdapterItem(),
        MapMarkerAdapterItem {
            analytics.sendClick("ListMarker_(${it.name})")
            store.accept(MapEvent.Ui.Action.OnListMarkerSelected(it))
        },
        ErrorStateAdapterItem {
            analytics.sendClick("MapReload")
            store.accept(MapEvent.Ui.Action.Reload)
        }
    )

    private fun createTabs() = listOf(
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_eat,
            nameResId = Strings.map_tab_name_eat,
            tab = FilterTab.FOOD
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_buildings,
            nameResId = Strings.map_tab_name_buildings,
            tab = FilterTab.BUILDINGS
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_hostels,
            nameResId = Strings.map_tab_name_hostels,
            tab = FilterTab.HOSTELS
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_others,
            nameResId = Strings.map_tab_name_others,
            tab = FilterTab.OTHERS
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_structure,
            nameResId = Strings.map_tab_name_structures,
            tab = FilterTab.STRUCTURES
        )
    )

    companion object {

        private const val MAX_OVERLAY_ALPHA = 0.5f
        private const val DEFAULT_CORNER_RADIUS = 16f // dp
    }
}
