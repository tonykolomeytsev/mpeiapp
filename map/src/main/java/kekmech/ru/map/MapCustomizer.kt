package kekmech.ru.map

import android.content.Context
import com.example.map.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions

class MapCustomizer(private val context: Context) {
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
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(55.755060, 37.708431), 17f))
        }
    }
}