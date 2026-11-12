package kekmech.ru.feature_map_impl.presentation.screen.main.elm

import kekmech.ru.feature_map_api.data.repository.MapRepository
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent.Internal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor

internal class MapActor(
    private val mapRepository: MapRepository,
) : Actor<MapCommand, MapEvent>() {

    override fun execute(command: MapCommand): Flow<MapEvent> = when (command) {
        is MapCommand.GetMapMarkers -> flow {
            // Show the saved markers right away instead of the loading, then update them
            val savedMarkers = mapRepository.getSavedMarkers().getOrNull()
            if (!savedMarkers.isNullOrEmpty()) emit(Internal.GetMapMarkersSuccess(savedMarkers))
            // getMarkers() falls back to the saved markers itself, so it fails only without them
            mapRepository.getMarkers().fold(
                onSuccess = { emit(Internal.GetMapMarkersSuccess(it)) },
                onFailure = { emit(Internal.GetMapMarkersFailure(it)) },
            )
        }
    }
}
