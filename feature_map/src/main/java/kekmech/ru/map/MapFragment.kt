package kekmech.ru.map


import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.views.setMargins
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.map.di.MapDependencies
import kekmech.ru.map.ext.animateCameraTo
import kekmech.ru.map.ext.init
import kekmech.ru.map.ext.toFilterTab
import kekmech.ru.map.ext.toMarkerType
import kekmech.ru.map.items.FilterTabItem
import kekmech.ru.map.items.MapMarkerAdapterItem
import kekmech.ru.map.items.TabBarAdapterItem
import kekmech.ru.map.presentation.*
import kekmech.ru.map.presentation.MapEvent.Wish
import kekmech.ru.map.view.ControlledScrollingLayoutManager
import kekmech.ru.map.view.MarkersBitmapFactory
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.android.ext.android.inject

private const val DEFAULT_NAVIGATION_DELAY = 1000L

internal class MapFragment : BaseFragment<MapEvent, MapEffect, MapState, MapFeature>() {

    override val initEvent = Wish.Init

    private val dependencies by inject<MapDependencies>()

    override fun createFeature() = dependencies.mapFeatureFactory.create()

    override var layoutId = R.layout.fragment_map

    private val adapter by fastLazy { createAdapter() }

    private val analytics: MapAnalytics by inject()

    private val markersBitmapFactory: MarkersBitmapFactory by inject()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        Handler().postDelayed({ createMap() }, 50L)
        recyclerView.layoutManager = ControlledScrollingLayoutManager(requireContext())
        recyclerView.adapter = adapter
        createBottomSheet(view)
        analytics.sendScreenShown()
    }

    private fun createBottomSheet(view: View) {
        view.doOnApplyWindowInsets { _, insets, padding ->
            coordinatorLayout.setMargins(top = insets.systemWindowInsetTop + padding.top)
        }
        BottomSheetBehavior.from(recyclerView).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) analytics.sendScroll("BottomSheet")
                    feature.accept(Wish.Action.BottomSheetStateChanged(newState))
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    viewFade.alpha = slideOffset * 0.5f
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
            it.init(requireContext())
            feature.accept(Wish.Action.OnMapReady(it))
        }
    }

    override fun render(state: MapState) {
        adapter.update(MapListConverter().map(state))
        val behavior = BottomSheetBehavior.from(recyclerView)
        (recyclerView.layoutManager as ControlledScrollingLayoutManager)
            .isScrollingEnabled = behavior.state != BottomSheetBehavior.STATE_COLLAPSED

        selectPlaceIfNecessary(state)
    }

    private fun selectPlaceIfNecessary(state: MapState) {
        state.googleMapMarkers.takeIf { it.isNotEmpty() } ?: return
        state.map ?: return
        val selectedPlaceUid = dependencies.selectedPlaceDelegate.get() ?: return
        val selectedMarker = state.markers.find { it.uid == selectedPlaceUid } ?: return
        val necessarySelectedTab = selectedMarker.type.toFilterTab() ?: return

        if (state.selectedTab != necessarySelectedTab) {
            feature.accept(Wish.Action.SelectTab(necessarySelectedTab))
            return
        }

        Handler().postDelayed({
            feature.accept(Wish.Action.OnListMarkerSelected(selectedMarker))
        }, DEFAULT_NAVIGATION_DELAY)
        dependencies.selectedPlaceDelegate.clear()
    }

    override fun handleEffect(effect: MapEffect) = when (effect) {
        is MapEffect.GenerateGoogleMapMarkers -> {
            val markers = generateGoogleMapMarkers(effect.map, effect.markers, effect.googleMapMarkers, effect.selectedTab)
            feature.accept(Wish.Action.GoogleMapMarkersGenerated(markers))
        }
        is MapEffect.AnimateCameraToPlace -> {
            effect.googleMapMarkers.find { it.title == effect.mapMarker.name }?.let { marker ->
                effect.map.animateCameraTo(marker)
                marker.showInfoWindow()
            }
            if (effect.collapseBottomSheet) BottomSheetBehavior.from(recyclerView)
                .state = BottomSheetBehavior.STATE_COLLAPSED
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
            .map { map.addMarker(
                MarkerOptions()
                    .title(it.name)
                    .position(LatLng(it.location.lat, it.location.lng))
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                        markersBitmapFactory.getBitmap(it)
                    ))
            ) }
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
