package kekmech.ru.map.ext

import android.annotation.SuppressLint
import android.content.Context
import com.example.map.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import kekmech.ru.common_android.dpToPx

@SuppressLint("MissingPermission")
fun GoogleMap.init(context: Context) {
    setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style))
    isMyLocationEnabled = false
    isBuildingsEnabled = true
    isTrafficEnabled = false
    isIndoorEnabled = true
    mapType = GoogleMap.MAP_TYPE_NORMAL
    setPadding(0, 0, 0, context.resources.dpToPx(120f))
    uiSettings.apply {
        isCompassEnabled = false
        isMyLocationButtonEnabled = false
        isZoomControlsEnabled = false
        isRotateGesturesEnabled = false
    }
    val cameraPosition = CameraPosition.Builder()
        .tilt(30f)
        .zoom(17f)
        .target(LatLng(55.755060, 37.708431)) // mpei main building
        .build()
    moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun GoogleMap.animateCameraTo(marker: Marker) {
    val cameraPosition = CameraPosition.Builder()
        .tilt(30f)
        .zoom(17f)
        .target(marker.position)
        .build()
    animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 200, null)
}