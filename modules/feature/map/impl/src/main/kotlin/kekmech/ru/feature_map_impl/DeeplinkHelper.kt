package kekmech.ru.feature_map_impl

import kekmech.ru.feature_map_impl.launcher.DeeplinkDelegate
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.toFilterTab
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private const val DEFAULT_NAVIGATION_DELAY = 1000L

/**
 * Call it only after the map is loaded, otherwise the camera has nothing to move to.
 */
internal object DeeplinkHelper {

    suspend fun handleDeeplinkIfNecessary(
        deeplinkDelegate: DeeplinkDelegate,
        state: MapState,
        onAccept: (MapEvent) -> Unit,
    ) {
        if (state.markers.isEmpty()) return
        selectPlaceIfNecessary(deeplinkDelegate, state, onAccept)
        selectTabIfNecessary(deeplinkDelegate, state, onAccept)
    }

    @Suppress("ReturnCount")
    private suspend fun selectPlaceIfNecessary(
        deeplinkDelegate: DeeplinkDelegate,
        state: MapState,
        onAccept: (MapEvent) -> Unit,
    ) {
        val selectedPlaceUid = deeplinkDelegate.getPlaceUid() ?: return
        val selectedMarker = state.markers.find { it.uid == selectedPlaceUid } ?: return
        val necessarySelectedTab = selectedMarker.type.toFilterTab() ?: return

        // Markers of the place's tab are drawn on the next composition, the place is selected then
        if (state.selectedTab != necessarySelectedTab) {
            onAccept(MapEvent.Ui.Action.SelectTab(necessarySelectedTab))
            return
        }

        deeplinkDelegate.clear()
        delay(DEFAULT_NAVIGATION_DELAY.milliseconds)
        onAccept(MapEvent.Ui.Action.OnListMarkerSelected(selectedMarker))
    }

    private fun selectTabIfNecessary(
        deeplinkDelegate: DeeplinkDelegate,
        state: MapState,
        onAccept: (MapEvent) -> Unit,
    ) {
        val necessarySelectedTab = deeplinkDelegate.getTab() ?: return

        if (state.selectedTab != necessarySelectedTab) {
            onAccept(MapEvent.Ui.Action.SelectTab(necessarySelectedTab))
        }
        deeplinkDelegate.clear()
    }
}
