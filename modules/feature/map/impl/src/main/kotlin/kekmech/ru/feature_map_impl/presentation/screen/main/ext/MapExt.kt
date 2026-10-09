package kekmech.ru.feature_map_impl.presentation.screen.main.ext

import android.content.Context
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

private const val CAMERA_ZOOM = 17.5
private const val MIN_ZOOM = 14.0
private const val MAX_ZOOM = 20.0
private const val DEFAULT_LAT = 55.755060
private const val DEFAULT_LNG = 37.708431

internal fun MapView.init(context: Context) {
    Configuration.getInstance().userAgentValue = context.packageName
    setTileSource(TileSourceFactory.MAPNIK)
    setMultiTouchControls(true)
    minZoomLevel = MIN_ZOOM
    maxZoomLevel = MAX_ZOOM
    controller.setZoom(CAMERA_ZOOM)
    controller.setCenter(GeoPoint(DEFAULT_LAT, DEFAULT_LNG))
}

internal fun MapView.animateCameraTo(marker: Marker) {
    controller.animateTo(marker.position, CAMERA_ZOOM, 300L)
}
