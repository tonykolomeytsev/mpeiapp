package kekmech.ru.feature_map_impl.presentation.screen.main.elm

import kekmech.ru.feature_map_api.data.repository.MapRepository
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor

internal class MapActor(
    private val mapRepository: MapRepository,
) : Actor<MapCommand, MapEvent>() {

    override fun execute(command: MapCommand): Flow<MapEvent> = when (command) {
        is MapCommand.GetMapMarkers -> actorFlow {
            mapRepository.getMarkers().getOrThrow()
        }.mapEvents(Internal::GetMapMarkersSuccess, Internal::GetMapMarkersFailure)
    }
}
