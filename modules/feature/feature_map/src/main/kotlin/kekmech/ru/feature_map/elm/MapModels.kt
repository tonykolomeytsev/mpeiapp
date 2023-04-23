package kekmech.ru.feature_map.elm

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

internal sealed interface MapEvent {

    sealed interface Ui : MapEvent {
        object Init : Ui

        object Action {
            data class OnMapReady(val map: GoogleMap) : Ui
            data class SelectTab(val tab: FilterTab) : Ui
            data class BottomSheetStateChanged(val newState: Int) : Ui
            data class GoogleMapMarkersGenerated(val googleMapMarkers: List<Marker>) : Ui
            data class OnListMarkerSelected(val mapMarker: MapMarker) : Ui
            object SilentUpdate : Ui
            object Reload : Ui
            object ScrollToTop : Ui
        }
    }

    sealed interface Internal : MapEvent {
        data class GetMapMarkersSuccess(val markers: List<MapMarker>) : Internal
        data class GetMapMarkersFailure(val throwable: Throwable) : Internal
    }
}

internal sealed interface MapEffect {
    data class GenerateGoogleMapMarkers(
        val map: GoogleMap?,
        val markers: List<MapMarker>?,
        val googleMapMarkers: List<Marker>,
        val selectedTab: FilterTab
    ) : MapEffect
    data class AnimateCameraToPlace(
        val map: GoogleMap,
        val googleMapMarkers: List<Marker>,
        val mapMarker: MapMarker,
        val collapseBottomSheet: Boolean
    ) : MapEffect
    object ShowLoadingError : MapEffect
}

internal sealed interface MapCommand {
    object GetMapMarkers : MapCommand
}
