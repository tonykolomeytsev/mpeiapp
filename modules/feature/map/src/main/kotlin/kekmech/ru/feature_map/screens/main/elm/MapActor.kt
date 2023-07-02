package kekmech.ru.feature_map.screens.main.elm

import kekmech.ru.domain_map.MapRepository
import kekmech.ru.feature_map.screens.main.elm.MapEvent.Internal
import kekmech.ru.library_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor

internal class MapActor(
    private val mapRepository: MapRepository,
) : Actor<MapCommand, MapEvent> {

    override fun execute(command: MapCommand): Flow<MapEvent> = when (command) {
        is MapCommand.GetMapMarkers -> actorFlow {
            mapRepository.getMarkers().getOrThrow()
        }.mapEvents(Internal::GetMapMarkersSuccess, Internal::GetMapMarkersFailure)
    }
}
