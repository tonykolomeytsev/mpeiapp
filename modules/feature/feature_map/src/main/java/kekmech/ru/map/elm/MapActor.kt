package kekmech.ru.map.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_map.MapRepository
import vivid.money.elmslie.core.store.Actor

internal class MapActor(
    private val mapRepository: MapRepository
) : Actor<MapAction, MapEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: MapAction): Observable<MapEvent> = when (action) {
        is MapAction.GetMarkers -> mapRepository.getMarkers()
            .mapEvents(MapEvent.News::MapMarkersLoaded, MapEvent.News::MapMarkersLoadError)
    }
}