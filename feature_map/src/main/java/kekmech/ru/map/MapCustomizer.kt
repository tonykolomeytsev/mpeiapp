package kekmech.ru.map

import android.content.Context
import android.util.Log
import com.example.map.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import kekmech.ru.core.FreqLimiter

class MapCustomizer(private val context: Context) {
    private val limiter = FreqLimiter()

    fun initMap(map: GoogleMap?) {
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
            val cameraPosition = CameraPosition.Builder()
                .tilt(30f)
                .zoom(17f)
                .target(LatLng(55.755060, 37.708431))
                .build()
            moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    fun animateCameraTo(map: GoogleMap?, marker: Marker) {
        Log.d("MapCustomizer", "animateCameraTo")
        if (!limiter()) return
        map?.apply {
            val cameraPosition = CameraPosition.Builder()
                .tilt(30f)
                .zoom(17f)
                .target(marker.position)
                .build()
            animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 200, null)
        }

    }
}