package kekmech.ru.map.presentation

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