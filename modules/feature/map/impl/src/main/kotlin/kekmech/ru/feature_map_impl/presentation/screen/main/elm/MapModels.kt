package kekmech.ru.feature_map_impl.presentation.screen.main.elm

import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_map_api.domain.model.MapMarker
internal data class MapState(
    val selectedTab: FilterTab = FilterTab.FOOD,
    val markers: List<MapMarker> = emptyList(),
    val bottomSheetState: Int = BottomSheetBehavior.STATE_COLLAPSED,
    val appSettings: AppSettings,
    val hash: String = "",
    val loadingError: Throwable? = null,
)

internal enum class FilterTab { FOOD, BUILDINGS, HOSTELS, OTHERS, STRUCTURES }

internal sealed interface MapEvent {

    sealed interface Ui : MapEvent {
        object Init : Ui

        object Action {
            data class SelectTab(val tab: FilterTab) : Ui
            data class BottomSheetStateChanged(val newState: Int) : Ui
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
    data class AnimateCameraToPlace(
        val mapMarker: MapMarker,
        val collapseBottomSheet: Boolean,
    ) : MapEffect

    object ShowLoadingError : MapEffect
}

internal sealed interface MapCommand {
    object GetMapMarkers : MapCommand
}
