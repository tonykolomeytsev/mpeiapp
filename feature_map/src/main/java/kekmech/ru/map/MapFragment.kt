package kekmech.ru.map


import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map.R
import com.google.android.gms.maps.SupportMapFragment
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.PullItem
import kekmech.ru.map.di.MapDependencies
import kekmech.ru.map.items.FilterTabItem
import kekmech.ru.map.items.TabBarAdapterItem
import kekmech.ru.map.items.TabBarItem
import kekmech.ru.map.presentation.*
import kekmech.ru.map.presentation.MapEvent.Wish
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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
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
        adapter.update(listOf(PullItem, TabBarItem))
    }

    override fun handleEffect(effect: MapEffect) {

    }

    private fun createAdapter() = BaseAdapter(
        PullAdapterItem(),
        TabBarAdapterItem(
            tabs = createTabs(),
            onClickListener = { println(it) }
        )
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
        )
    )
}
