package com.example.map

import android.content.Context
import android.util.Log
import android.util.TypedValue
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
import kekmech.ru.coreui.adapter.BaseAdapter
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
    }

    override fun onPause(view: MapFragmentView) {
        super.onPause(view)
        this.view = null
    }


    val Number.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)

    val GeoPoint.latLng get() = LatLng(this.latitude, this.longitude)

/*    private fun placeMarkers(it: List<Building>) {
        map?.apply {
            markers.forEach { it.remove() }
            markers.clear()
            lastSelectedBuilding = -1

            it.forEach {place ->
                val marker = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_placemark_building, FrameLayout(context), false)
                (marker.findViewById<TextView>(R.id.textViewPlacemarkTitle)).text = place.letter

                if (view != null) markers.add(addMarker(
                     MarkerOptions()
                        .position(place.location.toLatLng())
                        .title(place.name)
                        .icon(getMarkerIcon(view!!.contentView, place.letter, false))
                ))
            }
        }
    }*/
}

