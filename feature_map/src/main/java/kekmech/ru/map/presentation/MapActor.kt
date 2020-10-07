package kekmech.ru.map.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_map.MapRepository

internal class MapActor(
    private val mapRepository: MapRepository
) : Actor<MapAction, MapEvent> {

    override fun execute(action: MapAction): Observable<MapEvent> = when (action) {
        is MapAction.ObserveMarkers -> mapRepository.observeMarkers()
            .flatMap { Observable.just(it.markers) }
            .mapEvents(MapEvent.News::MapMarkersLoaded, MapEvent.News::MapMarkersLoadError)
    }
}