package kekmech.ru.feature_map.screens.main.ext

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import kekmech.ru.ext_android.dpToPx
import kekmech.ru.feature_map.R

private const val CAMERA_ANIMATION_DURATION = 200
private const val MAP_BOTTOM_PADDING = 120f // because of nav bar
private const val CAMERA_TILT = 30f
private const val CAMERA_ZOOM = 17f

@Suppress("MagicNumber")
@SuppressLint("MissingPermission")
internal fun GoogleMap.init(context: Context, mapAppearanceType: String, savedCameraPosition: CameraPosition?) {
    setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style))
    isMyLocationEnabled = false
    isBuildingsEnabled = true
    isTrafficEnabled = false
    isIndoorEnabled = true
    mapType = when (mapAppearanceType) {
        "scheme" -> GoogleMap.MAP_TYPE_NORMAL
        else -> GoogleMap.MAP_TYPE_HYBRID
    }
    setPadding(0, 0, 0, context.resources.dpToPx(MAP_BOTTOM_PADDING))
    uiSettings.apply {
        isCompassEnabled = false
        isMyLocationButtonEnabled = false
        isZoomControlsEnabled = false
        isRotateGesturesEnabled = false
        setMinZoomPreference(15f)
    }

    val cameraPosition = savedCameraPosition ?: CameraPosition.Builder()
        .tilt(CAMERA_TILT)
        .zoom(CAMERA_ZOOM)
        .target(LatLng(55.755060, 37.708431)) // mpei main building
        .build()

    moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

internal fun GoogleMap.animateCameraTo(marker: Marker) {
    val cameraPosition = CameraPosition.Builder()
        .tilt(CAMERA_TILT)
        .zoom(CAMERA_ZOOM)
        .target(marker.position)
        .build()
    animateCamera(
        CameraUpdateFactory.newCameraPosition(cameraPosition),
        CAMERA_ANIMATION_DURATION,
        null
    )
}
