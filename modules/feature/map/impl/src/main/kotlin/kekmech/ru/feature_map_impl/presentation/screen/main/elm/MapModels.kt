package kekmech.ru.feature_map_impl.presentation.screen.main.elm

import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_map_api.domain.model.MapMarker

internal data class MapState(
    val selectedTab: FilterTab = FilterTab.FOOD,
    val markers: List<MapMarker> = emptyList(),
    val appSettings: AppSettings,
    val loadingError: Throwable? = null,
)

internal enum class FilterTab { FOOD, BUILDINGS, HOSTELS, OTHERS, STRUCTURES }

internal sealed interface MapEvent {

    sealed interface Ui : MapEvent {
        object Init : Ui

        object Action {
            data class SelectTab(val tab: FilterTab) : Ui
            data class OnListMarkerSelected(val mapMarker: MapMarker) : Ui
            object Reload : Ui
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
