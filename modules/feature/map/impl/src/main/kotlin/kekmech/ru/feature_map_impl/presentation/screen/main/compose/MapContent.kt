package kekmech.ru.feature_map_impl.presentation.screen.main.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_impl.R

private const val CAMERA_ANIMATION_DURATION = 200
private const val CAMERA_TILT = 30f
private const val CAMERA_ZOOM = 17f
private const val MIN_ZOOM = 15f
private const val MAP_TYPE_SCHEME = "scheme"
private val MpeiMainBuilding = LatLng(55.755060, 37.708431)

internal val DefaultCameraPosition: CameraPosition = CameraPosition.Builder()
    .tilt(CAMERA_TILT)
    .zoom(CAMERA_ZOOM)
    .target(MpeiMainBuilding)
    .build()

@SuppressLint("MissingPermission")
@Composable
internal fun MapContent(
    markers: List<MapMarker>,
    markerStates: Map<String, MarkerState>,
    cameraPositionState: CameraPositionState,
    mapAppearanceType: String,
    markerIcon: (MapMarker) -> BitmapDescriptor,
    bottomPadding: Dp,
    onMapLoaded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val properties = remember(mapAppearanceType) {
        MapProperties(
            isBuildingEnabled = true,
            isIndoorEnabled = true,
            isMyLocationEnabled = false,
            isTrafficEnabled = false,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style),
            mapType = if (mapAppearanceType == MAP_TYPE_SCHEME) MapType.NORMAL else MapType.HYBRID,
            minZoomPreference = MIN_ZOOM,
        )
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = DefaultMapUiSettings,
        onMapLoaded = onMapLoaded,
        contentPadding = PaddingValues(bottom = bottomPadding),
    ) {
        markers.forEach { marker ->
            val markerState = markerStates[marker.uid] ?: return@forEach
            // Without a key, marker nodes are reused by position after a tab switch and stay bound
            // to MarkerStates of the previous tab, so showInfoWindow() of the new ones does nothing
            key(marker.uid) {
                val icon = remember(marker) { markerIcon(marker) }
                Marker(
                    state = markerState,
                    title = marker.name,
                    snippet = marker.address,
                    icon = icon,
                )
            }
        }
    }
}

internal suspend fun CameraPositionState.animateTo(position: LatLng) {
    val cameraPosition = CameraPosition.Builder()
        .tilt(CAMERA_TILT)
        .zoom(CAMERA_ZOOM)
        .target(position)
        .build()
    animate(CameraUpdateFactory.newCameraPosition(cameraPosition), CAMERA_ANIMATION_DURATION)
}

private val DefaultMapUiSettings = MapUiSettings(
    compassEnabled = false,
    myLocationButtonEnabled = false,
    rotationGesturesEnabled = false,
    zoomControlsEnabled = false,
)
