package kekmech.ru.feature_map_impl.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.ErrorStateAdapterItem
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.ext_android.doOnApplyWindowInsets
import kekmech.ru.ext_android.dpToPx
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.ext_android.parcelable
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
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.animateCameraTo
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

    private val store by androidElmStore(
        viewModelStoreOwner = { requireActivity() },
        savedStateRegistryOwner = { requireActivity() },
    ) { inject<MapStoreFactory>().value.create() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isMapStateEmpty()) {
            view.postDelayed(::createMap, MAP_START_UP_DELAY)
        } else {
            createMap()
        }

        viewBinding.recyclerView.layoutManager = ControlledScrollingLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.background = bottomSheetBackground

        createBottomSheet(view)
    }

    override fun onDestroyView() {
        saveMapState()
        super.onDestroyView()
    }

    private fun createBottomSheet(view: View) {
        view.doOnApplyWindowInsets { _, insets, padding ->
            viewBinding.coordinatorLayout.setMargins(top = insets.systemWindowInsetTop + padding.top)
        }
        @Suppress("UnnecessaryApply")
        BottomSheetBehavior.from(viewBinding.recyclerView).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        analytics.sendScroll("MapBottomSheet")
                    }
                    store.accept(MapEvent.Ui.Action.BottomSheetStateChanged(newState))
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    viewBinding.viewFade.alpha = slideOffset * MAX_OVERLAY_ALPHA
                }
            })
        }
    }

    private fun createMap() {
        val mapFragment = SupportMapFragment()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.mapFragmentContainer, mapFragment)
            .commitAllowingStateLoss()
        mapFragment.getMapAsync { googleMap ->
            googleMap.init(
                context = requireContext(),
                mapAppearanceType = appSettingsRepository
                    .getAppSettings()
                    .mapAppearanceType,
                savedCameraPosition = getSavedCameraPosition(),
            )
            store.accept(MapEvent.Ui.Action.OnMapReady(googleMap))
        }
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
        DeeplinkHelper.handleDeeplinkIfNecessary(dependencies.deeplinkDelegate, state, store)
    }

    override fun handleEffect(effect: MapEffect) =
        when (effect) {
            is MapEffect.GenerateGoogleMapMarkers -> {
                val markers = generateGoogleMapMarkers(
                    map = effect.map,
                    markers = effect.markers,
                    googleMapMarkers = effect.googleMapMarkers,
                    selectedTab = effect.selectedTab
                )
                store.accept(MapEvent.Ui.Action.GoogleMapMarkersGenerated(markers))
            }

            is MapEffect.AnimateCameraToPlace -> {
                effect.googleMapMarkers.find { it.title == effect.mapMarker.name }?.let { marker ->
                    effect.map.animateCameraTo(marker)
                    marker.showInfoWindow()
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

    private fun generateGoogleMapMarkers(
        map: GoogleMap?,
        markers: List<MapMarker>?,
        googleMapMarkers: List<Marker>,
        selectedTab: FilterTab,
    ): List<Marker> {
        if (markers.isNullOrEmpty() || map == null) return emptyList()
        googleMapMarkers.forEach { it.remove() }
        map.clear()
        return markers
            .filter { it.type == selectedTab.toMarkerType() }
            .mapNotNull {
                map.addMarker(
                    MarkerOptions()
                        .title(it.name)
                        .snippet(it.address)
                        .position(LatLng(it.location.lat, it.location.lng))
                        .icon(
                            BitmapDescriptorFactory.fromBitmap(
                                markersBitmapFactory.getBitmap(it)
                            )
                        )
                )
            }
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

    private fun saveMapState() {
        childFragmentManager.fragments.firstOrNull()?.onSaveInstanceState(stateBundle)
    }

    private fun getSavedCameraPosition(): CameraPosition? =
        stateBundle.getBundle("map_state")?.parcelable("camera")

    private fun isMapStateEmpty(): Boolean =
        stateBundle.getBundle("map_state") == null

    companion object {

        private const val MAP_START_UP_DELAY = 100L // ms
        private const val MAX_OVERLAY_ALPHA = 0.5f
        private const val DEFAULT_CORNER_RADIUS = 16f // dp
    }
}
