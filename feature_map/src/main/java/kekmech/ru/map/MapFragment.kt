package kekmech.ru.map


import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.map.R
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.views.setMargins
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.map.di.MapDependencies
import kekmech.ru.map.items.FilterTabItem
import kekmech.ru.map.items.MapMarkerAdapterItem
import kekmech.ru.map.items.TabBarAdapterItem
import kekmech.ru.map.presentation.*
import kekmech.ru.map.presentation.MapEvent.Wish
import kekmech.ru.map.view.ControlledScrollingLayoutManager
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.android.ext.android.inject


class MapFragment : BaseFragment<MapEvent, MapEffect, MapState, MapFeature>() {

    override val initEvent = Wish.Init

    private val dependencies by inject<MapDependencies>()

    override fun createFeature() = dependencies.mapFeatureFactory.create()

    override var layoutId = R.layout.fragment_map

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        Handler().postDelayed({ createMap() }, 50L)
        recyclerView.layoutManager = ControlledScrollingLayoutManager(requireContext())
        recyclerView.adapter = adapter
        createBottomSheet(view)
    }

    private fun createBottomSheet(view: View) {
        view.doOnApplyWindowInsets { _, insets, padding ->
            coordinatorLayout.setMargins(top = insets.systemWindowInsetTop + padding.top)
        }
        BottomSheetBehavior.from(recyclerView).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
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
        (recyclerView.layoutManager as ControlledScrollingLayoutManager).apply {
            val realState = BottomSheetBehavior.from(recyclerView).state
            isScrollingEnabled = realState != BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun handleEffect(effect: MapEffect) {

    }

    private fun createAdapter() = BaseAdapter(
        PullAdapterItem(),
        TabBarAdapterItem(
            tabs = createTabs(),
            onClickListener = { feature.accept(Wish.Action.SelectTab(it)) }
        ),
        SectionHeaderAdapterItem(),
        SpaceAdapterItem(),
        MapMarkerAdapterItem { /* no-op */ }
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
