package kekmech.ru.feature_map_impl.presentation.screen.main.ext

import android.content.Context
import android.graphics.Color
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay

private const val CAMERA_ZOOM = 17.5
private const val MIN_ZOOM = 14.0
private const val MAX_ZOOM = 20.0
private const val DEFAULT_LAT = 55.755060
private const val DEFAULT_LNG = 37.708431

internal fun MapView.init(context: Context) {
    Configuration.getInstance().userAgentValue = context.packageName
    setMultiTouchControls(true)
    minZoomLevel = MIN_ZOOM
    maxZoomLevel = MAX_ZOOM
    controller.setZoom(CAMERA_ZOOM)
    controller.setCenter(GeoPoint(DEFAULT_LAT, DEFAULT_LNG))
}

internal fun MapView.animateCameraTo(marker: Marker) {
    controller.animateTo(marker.position, CAMERA_ZOOM, 300L)
}

/** What the map shows: street scheme or satellite imagery with street names on top. */
internal enum class MapAppearance(val code: String) {
    SCHEME("scheme"),
    HYBRID("hybrid");

    companion object {
        fun fromCode(code: String?): MapAppearance = values().firstOrNull { it.code == code } ?: SCHEME
    }
}

private const val ESRI_MAX_ZOOM = 19
private const val TILE_SIZE = 256
private const val ESRI_COPYRIGHT = "© Esri, Maxar, Earthstar Geographics"
private const val ESRI_BASE = "https://server.arcgisonline.com/ArcGIS/rest/services/"

/** Esri ArcGIS tiles are addressed as `.../tile/{z}/{y}/{x}` (row before column) */
private class EsriTileSource(name: String, service: String, extension: String) : OnlineTileSourceBase(
    name,
    0,
    ESRI_MAX_ZOOM,
    TILE_SIZE,
    extension,
    arrayOf("$ESRI_BASE$service/MapServer/tile/"),
    ESRI_COPYRIGHT,
) {
    override fun getTileURLString(pMapTileIndex: Long): String =
        baseUrl +
            MapTileIndex.getZoom(pMapTileIndex) + "/" +
            MapTileIndex.getY(pMapTileIndex) + "/" +
            MapTileIndex.getX(pMapTileIndex)
}

private val SatelliteSource = EsriTileSource("EsriWorldImagery", "World_Imagery", ".jpg")
private val LabelsSource = EsriTileSource("EsriBoundariesAndPlaces", "Reference/World_Boundaries_and_Places", ".png")

/**
 * Switches the base layer of the map and redraws it.
 *
 * @param labels the street names overlay that was added by the previous call, null if there was none
 * @return the overlay added by this call (to be passed to the next call), null for the scheme
 */
internal fun MapView.applyAppearance(appearance: MapAppearance, labels: TilesOverlay?): TilesOverlay? {
    labels?.let {
        overlays.remove(it)
        it.onDetach(this)
    }
    val newLabels = when (appearance) {
        MapAppearance.SCHEME -> {
            setTileSource(TileSourceFactory.MAPNIK)
            null
        }

        MapAppearance.HYBRID -> {
            setTileSource(SatelliteSource)
            TilesOverlay(MapTileProviderBasic(context, LabelsSource), context).apply {
                loadingBackgroundColor = Color.TRANSPARENT
                loadingLineColor = Color.TRANSPARENT
                // below the markers, above the satellite imagery
                overlays.add(0, this)
            }
        }
    }
    invalidate()
    return newLabels
}

internal fun MapAppearance.copyright(): String? = if (this == MapAppearance.HYBRID) ESRI_COPYRIGHT else null
