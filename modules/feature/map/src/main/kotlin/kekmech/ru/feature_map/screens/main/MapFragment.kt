package kekmech.ru.feature_map.screens.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.views.setMargins
import kekmech.ru.common_elm.BaseFragment
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_navigation.features.ScrollToTop
import kekmech.ru.common_navigation.features.TabScreenStateSaver
import kekmech.ru.common_navigation.features.TabScreenStateSaverImpl
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.ErrorStateAdapterItem
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.feature_map.DeeplinkHelper
import kekmech.ru.feature_map.R
import kekmech.ru.feature_map.databinding.FragmentMapBinding
import kekmech.ru.feature_map.di.MapDependencies
import kekmech.ru.feature_map.items.FilterTabItem
import kekmech.ru.feature_map.items.MapMarkerAdapterItem
import kekmech.ru.feature_map.items.TabBarAdapterItem
import kekmech.ru.feature_map.screens.main.elm.FilterTab
import kekmech.ru.feature_map.screens.main.elm.MapEffect
import kekmech.ru.feature_map.screens.main.elm.MapEvent
import kekmech.ru.feature_map.screens.main.elm.MapState
import kekmech.ru.feature_map.screens.main.elm.MapStoreProvider
import kekmech.ru.feature_map.screens.main.ext.animateCameraTo
import kekmech.ru.feature_map.screens.main.ext.init
import kekmech.ru.feature_map.screens.main.ext.toMarkerType
import kekmech.ru.feature_map.screens.main.view.BottomSheetBackgroundDrawable
import kekmech.ru.feature_map.screens.main.view.ControlledScrollingLayoutManager
import kekmech.ru.feature_map.screens.main.view.MarkersBitmapFactory
import kekmech.ru.strings.Strings
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
internal class MapFragment : BaseFragment<MapEvent, MapEffect, MapState>(R.layout.fragment_map),
    ScrollToTop,
    TabScreenStateSaver by TabScreenStateSaverImpl("map") {

    private val dependencies by inject<MapDependencies>()
    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("Map")
    private val markersBitmapFactory: MarkersBitmapFactory by inject()
    private val viewBinding by viewBinding(FragmentMapBinding::bind)
    private val bottomSheetBackground by fastLazy {
        BottomSheetBackgroundDrawable(
            backgroundColor = requireContext().getThemeColor(kekmech.ru.coreui.R.attr.colorWhite),
            topCornerRadius = resources.dpToPx(DEFAULT_CORNER_RADIUS).toFloat()
        )
    }
    private val appSettingsRepository by inject<AppSettingsRepository>()

    override fun createStore() = inject<MapStoreProvider>().value.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isMapStateEmpty()) {
            Completable.timer(MAP_START_UP_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::createMap)
                .bind()
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
        stateBundle.getBundle("map_state")?.getParcelable("camera")

    private fun isMapStateEmpty(): Boolean =
        stateBundle.getBundle("map_state") == null

    companion object {

        private const val MAP_START_UP_DELAY = 100L // ms
        private const val MAX_OVERLAY_ALPHA = 0.5f
        private const val DEFAULT_CORNER_RADIUS = 16f // dp
    }
}
