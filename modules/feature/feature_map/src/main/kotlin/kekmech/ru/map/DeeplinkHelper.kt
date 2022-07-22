package kekmech.ru.map

import android.os.Handler
import android.os.Looper
import kekmech.ru.map.elm.MapEffect
import kekmech.ru.map.elm.MapEvent
import kekmech.ru.map.elm.MapState
import kekmech.ru.map.ext.toFilterTab
import kekmech.ru.map.launcher.DeeplinkDelegate
import vivid.money.elmslie.core.store.Store

private const val DEFAULT_NAVIGATION_DELAY = 1000L

internal object DeeplinkHelper {

    fun handleDeeplinkIfNecessary(
        deeplinkDelegate: DeeplinkDelegate,
        state: MapState,
        feature: Store<MapEvent, MapEffect, MapState>
    ) {
        selectPlaceIfNecessary(deeplinkDelegate, state, feature)
        selectTabIfNecessary(deeplinkDelegate, state, feature)
    }

    @Suppress("ReturnCount")
    private fun selectPlaceIfNecessary(
        deeplinkDelegate: DeeplinkDelegate,
        state: MapState,
        feature: Store<MapEvent, MapEffect, MapState>
    ) {
        state.googleMapMarkers.takeIf { it.isNotEmpty() } ?: return
        state.map ?: return
        val selectedPlaceUid = deeplinkDelegate.getPlaceUid() ?: return
        val selectedMarker = state.markers.find { it.uid == selectedPlaceUid } ?: return
        val necessarySelectedTab = selectedMarker.type.toFilterTab() ?: return

        if (state.selectedTab != necessarySelectedTab) {
            feature.accept(MapEvent.Wish.Action.SelectTab(necessarySelectedTab))
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            feature.accept(MapEvent.Wish.Action.OnListMarkerSelected(selectedMarker))
        }, DEFAULT_NAVIGATION_DELAY)
        deeplinkDelegate.clear()
    }

    private fun selectTabIfNecessary(
        deeplinkDelegate: DeeplinkDelegate,
        state: MapState,
        feature: Store<MapEvent, MapEffect, MapState>
    ) {
        state.googleMapMarkers.takeIf { it.isNotEmpty() } ?: return
        state.map ?: return
        val necessarySelectedTab = deeplinkDelegate.getTab() ?: return

        if (state.selectedTab != necessarySelectedTab) {
            feature.accept(MapEvent.Wish.Action.SelectTab(necessarySelectedTab))
        }
        deeplinkDelegate.clear()
    }

}
