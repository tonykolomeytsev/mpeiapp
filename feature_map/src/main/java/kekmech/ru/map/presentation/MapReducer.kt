package kekmech.ru.map.presentation

import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.map.presentation.MapEvent.News
import kekmech.ru.map.presentation.MapEvent.Wish

typealias MapResult = Result<MapState, MapEffect, MapAction>

class MapReducer : BaseReducer<MapState, MapEvent, MapEffect, MapAction> {

    override fun reduce(
        event: MapEvent,
        state: MapState
    ): MapResult = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceWish(
        event: Wish,
        state: MapState
    ): Result<MapState, MapEffect, MapAction> = when (event) {
        is Wish.Init -> Result(
            state = state,
            action = MapAction.ObserveMarkers
        )
        is Wish.Action.OnMapReady -> Result(
            state = state.copy(
                map = event.map
            ),
            effect = MapEffect.GenerateGoogleMapMarkers(event.map, state.markers, state.googleMapMarkers, state.selectedTab)
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
        is Wish.Action.OnListMarkerSelected -> {
            val newBottomSheetState = if (state.appSettings.autoHideBottomSheet) {
                BottomSheetBehavior.STATE_COLLAPSED
            } else {
                state.bottomSheetState
            }
            Result(
                state = state.copy(bottomSheetState = newBottomSheetState),
                effect = state.map?.let { MapEffect.AnimateCameraToPlace(it, state.googleMapMarkers, event.mapMarker) }
            )
        }
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