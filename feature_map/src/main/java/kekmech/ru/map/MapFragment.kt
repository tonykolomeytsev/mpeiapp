package kekmech.ru.map


import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.map.R
import com.google.android.gms.maps.SupportMapFragment
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.map.di.MapDependencies
import kekmech.ru.map.presentation.MapEffect
import kekmech.ru.map.presentation.MapEvent
import kekmech.ru.map.presentation.MapEvent.Wish
import kekmech.ru.map.presentation.MapFeature
import kekmech.ru.map.presentation.MapState
import org.koin.android.ext.android.inject


class MapFragment : BaseFragment<MapEvent, MapEffect, MapState, MapFeature>() {

    override val initEvent = Wish.Init

    private val dependencies by inject<MapDependencies>()

    override fun createFeature() = dependencies.mapFeatureFactory.create()

    override var layoutId = R.layout.fragment_map

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        Handler().postDelayed({ createMap() }, 50L)
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

    }

    override fun handleEffect(effect: MapEffect) {

    }
}
