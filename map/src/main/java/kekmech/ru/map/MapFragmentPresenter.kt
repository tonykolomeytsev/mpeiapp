package com.example.map

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.map.model.MapFragmentModel
import com.example.map.view.MapFragmentView
import com.example.map.view.pages.BuildingsItem
import com.example.map.view.pages.FoodsItem
import com.example.map.view.pages.HostelsItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import kekmech.ru.core.Presenter
import kekmech.ru.core.dto.Building
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.map.view.CustomMarkerView
import javax.inject.Inject


class MapFragmentPresenter @Inject constructor(
    private val model: MapFragmentModel,
    private val context: Context
) : Presenter<MapFragmentView>(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var view: MapFragmentView? = null
    private var mapCustomizer = MapCustomizer(context)
    private var mapUIAdapter = BaseAdapter.Builder()
        .registerViewTypeFactory(BuildingsItem.Factory())
        .registerViewTypeFactory(HostelsItem.Factory())
        .registerViewTypeFactory(FoodsItem.Factory())
        .build()
        .apply { baseItems.addAll(listOf(BuildingsItem(model), HostelsItem(model), FoodsItem(model))) }

    override fun onMapReady(map: GoogleMap?) {
        Log.d("MapFragmentPresenter", map.toString())
        this.map = map
        mapCustomizer.initMap(map)
        view?.placeContentUnderStatusBar()
    }

    override fun onResume(view: MapFragmentView) {
        super.onResume(view)
        this.view = view
        view.setAdapter(mapUIAdapter)
        view.setState(model.state)
        view.onChangeStateListener = {
            model.state = it
            replaceMarkers(view)
        }
        replaceMarkers(view)
    }

    override fun onPause(view: MapFragmentView) {
        super.onPause(view)
        this.view = null
    }


    val Number.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)

    val GeoPoint.latLng get() = LatLng(this.latitude, this.longitude)


    private fun placeBuildingMarkers(places: List<Building>) {
        map?.apply {
            model.markers.forEach { it.remove() }
            model.markers.clear()

            places.forEachIndexed { i, place ->
                if (view != null) model.markers.add(addMarker(
                     MarkerOptions()
                        .position(place.location.latLng)
                        .title(place.name)
                        .icon(CustomMarkerView(context, R.layout.item_placemark_building) {
                            (it.findViewById<TextView>(R.id.textViewPlacemarkTitle)).text = place.letter
                        }.getMarkerIcon())
                ))
            }
        }
    }

    private fun replaceMarkers(view: MapFragmentView) {
        if (model.state == MapFragmentModel.PAGE_BUILDINGS) {
            model.buildings.observe(view, Observer { placeBuildingMarkers(it) })
        } else {
            model.markers.forEach { it.remove() }
            model.markers.clear()
        }
    }
}

