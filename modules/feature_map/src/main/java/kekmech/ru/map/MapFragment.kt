package kekmech.ru.map

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.views.setMargins
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.NeedToUpdate
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.map.databinding.FragmentMapBinding
import kekmech.ru.map.di.MapDependencies
import kekmech.ru.map.elm.FilterTab
import kekmech.ru.map.elm.MapEffect
import kekmech.ru.map.elm.MapEvent
import kekmech.ru.map.elm.MapEvent.Wish
import kekmech.ru.map.elm.MapState
import kekmech.ru.map.ext.animateCameraTo
import kekmech.ru.map.ext.init
import kekmech.ru.map.ext.toMarkerType
import kekmech.ru.map.items.FilterTabItem
import kekmech.ru.map.items.MapMarkerAdapterItem
import kekmech.ru.map.items.TabBarAdapterItem
import kekmech.ru.map.view.BottomSheetBackgroundDrawable
import kekmech.ru.map.view.ControlledScrollingLayoutManager
import kekmech.ru.map.view.MarkersBitmapFactory
import org.koin.android.ext.android.inject

private const val MAP_CREATION_DELAY = 50L
private const val MAX_OVERLAY_ALPHA = 0.5f
private const val DEFAULT_CORNER_RADIUS = 16f // dp

internal class MapFragment : BaseFragment<MapEvent, MapEffect, MapState>(), NeedToUpdate {

    override val initEvent = Wish.Init
    override val layoutId = R.layout.fragment_map

    private val dependencies by inject<MapDependencies>()
    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("Map")
    private val markersBitmapFactory: MarkersBitmapFactory by inject()
    private val viewBinding by viewBinding(FragmentMapBinding::bind)
    private val bottomSheetBackground by fastLazy { BottomSheetBackgroundDrawable(
        backgroundColor = requireContext().getThemeColor(R.attr.colorWhite),
        topCornerRadius = resources.dpToPx(DEFAULT_CORNER_RADIUS).toFloat()
    ) }
    private val appSettings by inject<AppSettings>()

    override fun createStore() = dependencies.mapFeatureFactory.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({ createMap() }, MAP_CREATION_DELAY)
        viewBinding.recyclerView.layoutManager = ControlledScrollingLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.background = bottomSheetBackground
        createBottomSheet(view)
    }

    private fun createBottomSheet(view: View) {
        view.doOnApplyWindowInsets { _, insets, padding ->
            viewBinding.coordinatorLayout.setMargins(top = insets.systemWindowInsetTop + padding.top)
        }
        BottomSheetBehavior.from(viewBinding.recyclerView).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        analytics.sendScroll("MapBottomSheet")
                    }
                    feature.accept(Wish.Action.BottomSheetStateChanged(newState))
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
        mapFragment.getMapAsync {
            it.init(requireContext(), appSettings.mapAppearanceType)
            feature.accept(Wish.Action.OnMapReady(it))
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
        DeeplinkHelper.handleDeeplinkIfNecessary(dependencies.deeplinkDelegate, state, feature)
    }

    override fun handleEffect(effect: MapEffect) = when (effect) {
        is MapEffect.GenerateGoogleMapMarkers -> {
            val markers = generateGoogleMapMarkers(
                map = effect.map,
                markers = effect.markers,
                googleMapMarkers = effect.googleMapMarkers,
                selectedTab = effect.selectedTab
            )
            feature.accept(Wish.Action.GoogleMapMarkersGenerated(markers))
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
    }

    private fun generateGoogleMapMarkers(
        map: GoogleMap?,
        markers: List<MapMarker>?,
        googleMapMarkers: List<Marker>,
        selectedTab: FilterTab
    ): List<Marker> {
        if (markers.isNullOrEmpty() || map == null) return emptyList()
        googleMapMarkers.forEach { it.remove() }
        map.clear()
        return markers
            .filter { it.type == selectedTab.toMarkerType() }
            .mapNotNull { map.addMarker(
                MarkerOptions()
                    .title(it.name)
                    .position(LatLng(it.location.lat, it.location.lng))
                    .icon(BitmapDescriptorFactory.fromBitmap(
                        markersBitmapFactory.getBitmap(it)
                    ))
            ) }
    }

    override fun onUpdate() {
        if (dependencies.deeplinkDelegate.isNotEmpty()) {
            feature.accept(Wish.Action.SilentUpdate)
        }
    }

    private fun createAdapter() = BaseAdapter(
        PullAdapterItem(),
        TabBarAdapterItem(
            tabs = createTabs(),
            onClickListener = {
                analytics.sendClick("Tab_$it")
                feature.accept(Wish.Action.SelectTab(it))
            }
        ),
        SectionHeaderAdapterItem(),
        SpaceAdapterItem(),
        MapMarkerAdapterItem {
            analytics.sendClick("ListMarker_(${it.name})")
            feature.accept(Wish.Action.OnListMarkerSelected(it))
        }
    )

    private fun createTabs() = listOf(
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_eat,
            nameResId = R.string.map_tab_name_eat,
            tab = FilterTab.FOOD
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_buildings,
            nameResId = R.string.map_tab_name_buildings,
            tab = FilterTab.BUILDINGS
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_hostels,
            nameResId = R.string.map_tab_name_hostels,
            tab = FilterTab.HOSTELS
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_others,
            nameResId = R.string.map_tab_name_others,
            tab = FilterTab.OTHERS
        ),
        FilterTabItem(
            drawableResId = R.drawable.ic_map_tab_structure,
            nameResId = R.string.map_tab_name_structures,
            tab = FilterTab.STRUCTURES
        )
    )
}
