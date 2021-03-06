package kekmech.ru.map.elm

import kekmech.ru.map.elm.MapEvent.News
import kekmech.ru.map.elm.MapEvent.Wish
import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Result
import java.util.*

internal class MapReducer : StateReducer<MapEvent, MapState, MapEffect, MapAction> {

    override fun reduce(
        event: MapEvent,
        state: MapState
    ): Result<MapState, MapEffect, MapAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceWish(
        event: Wish,
        state: MapState
    ): Result<MapState, MapEffect, MapAction> = when (event) {
        is Wish.Init -> Result(
            state = state,
            command = MapAction.ObserveMarkers
        )
        is Wish.Action.OnMapReady -> Result(
            state = state.copy(
                map = event.map
            ),
            effect = MapEffect.GenerateGoogleMapMarkers(
                map = event.map,
                markers = state.markers,
                googleMapMarkers = state.googleMapMarkers,
                selectedTab = state.selectedTab
            )
        )
        is Wish.Action.SelectTab -> Result(
            state = state.copy(
                selectedTab = event.tab
            ),
            effect = MapEffect.GenerateGoogleMapMarkers(state.map, state.markers, state.googleMapMarkers, event.tab)
        )
        is Wish.Action.BottomSheetStateChanged -> Result(
            state = state.copy(bottomSheetState = event.newState)
        )
        is Wish.Action.GoogleMapMarkersGenerated -> Result(
            state = state.copy(googleMapMarkers = event.googleMapMarkers)
        )
        is Wish.Action.OnListMarkerSelected -> Result(
            state = state,
            effect = state.map?.let { map ->
                MapEffect.AnimateCameraToPlace(
                    map = map,
                    googleMapMarkers = state.googleMapMarkers,
                    mapMarker = event.mapMarker,
                    collapseBottomSheet = state.appSettings.autoHideBottomSheet
                )
            }
        )
        is Wish.Action.SilentUpdate -> Result(
            state = state.copy(hash = UUID.randomUUID().toString())
        )
    }

    private fun reduceNews(
        event: News,
        state: MapState
    ): Result<MapState, MapEffect, MapAction> = when (event) {
        is News.MapMarkersLoaded -> Result(
            state = state.copy(
                markers = event.markers
            )
        )
        is News.MapMarkersLoadError -> Result(state = state)
    }
}