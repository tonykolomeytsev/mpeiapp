package com.example.map

import android.content.Context
import android.util.Log
import com.example.map.model.MapFragmentModel
import com.example.map.view.MapFragmentView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import kekmech.ru.core.Presenter
import javax.inject.Inject

class MapFragmentPresenter @Inject constructor(
    private val model: MapFragmentModel,
    private val context: Context
) : Presenter<MapFragmentView>(), OnMapReadyCallback {
    private var map: GoogleMap? = null

    override fun onMapReady(map: GoogleMap?) {
        Log.d("MapFragmentPresenter", map.toString())
        this.map = map
        map?.apply {
            setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style))
            isMyLocationEnabled = false
            isBuildingsEnabled = true
            isTrafficEnabled = false
            isIndoorEnabled = true
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.apply {
                isCompassEnabled = false
                isMyLocationButtonEnabled = false
                isZoomControlsEnabled = false
                isRotateGesturesEnabled = false
            }
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(55.755060, 37.708431), 17f))
        }
    }

    override fun onResume(view: MapFragmentView) {
        super.onResume(view)
        model.buildings
    }
}