package kekmech.ru.feature_map.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.feature_map.elm.MapEvent.Internal
import vivid.money.elmslie.core.store.Actor

internal class MapActor(
    private val mapRepository: MapRepository
) : Actor<MapCommand, MapEvent> {

    override fun execute(command: MapCommand): Observable<MapEvent> = when (command) {
        is MapCommand.GetMapMarkers -> mapRepository.getMarkers()
            .mapEvents(Internal::GetMapMarkersSuccess, Internal::GetMapMarkersFailure)
    }
}
