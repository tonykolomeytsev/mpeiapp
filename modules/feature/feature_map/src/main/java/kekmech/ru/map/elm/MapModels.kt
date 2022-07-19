package kekmech.ru.map.elm

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_map.dto.MapMarker

internal data class MapState(
    val selectedTab: FilterTab = FilterTab.FOOD,
    val markers: List<MapMarker> = emptyList(),
    val map: GoogleMap? = null,
    val bottomSheetState: Int = BottomSheetBehavior.STATE_COLLAPSED,
    val googleMapMarkers: List<Marker> = emptyList(),
    val appSettings: AppSettings,
    val hash: String = "",
    val loadingError: Throwable? = null,
)

internal enum class FilterTab { FOOD, BUILDINGS, HOSTELS, OTHERS, STRUCTURES }

internal sealed class MapEvent {

    sealed class Wish : MapEvent() {
        object Init : Wish()

        object Action {
            data class OnMapReady(val map: GoogleMap) : Wish()
            data class SelectTab(val tab: FilterTab) : Wish()
            data class BottomSheetStateChanged(val newState: Int) : Wish()
            data class GoogleMapMarkersGenerated(val googleMapMarkers: List<Marker>) : Wish()
            data class OnListMarkerSelected(val mapMarker: MapMarker) : Wish()
            object SilentUpdate : Wish()
            object Reload : Wish()
            object ScrollToTop : Wish()
        }
    }

    sealed class News : MapEvent() {
        data class MapMarkersLoaded(val markers: List<MapMarker>) : News()
        data class MapMarkersLoadError(val throwable: Throwable) : News()
    }
}

internal sealed class MapEffect {
    data class GenerateGoogleMapMarkers(
        val map: GoogleMap?,
        val markers: List<MapMarker>?,
        val googleMapMarkers: List<Marker>,
        val selectedTab: FilterTab
    ) : MapEffect()
    data class AnimateCameraToPlace(
        val map: GoogleMap,
        val googleMapMarkers: List<Marker>,
        val mapMarker: MapMarker,
        val collapseBottomSheet: Boolean
    ) : MapEffect()
    object ShowLoadingError : MapEffect()
}

internal sealed class MapAction {
    object GetMarkers : MapAction()
}