package kekmech.ru.feature_map_impl.presentation.screen.main.elm

import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent.Internal
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapCommand as Command
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEffect as Effect
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent as Event
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState as State

internal class MapReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.GetMapMarkersSuccess -> state {
                copy(
                    markers = event.markers,
                    loadingError = null,
                )
            }

            is Internal.GetMapMarkersFailure -> {
                state { copy(loadingError = event.throwable) }
                effects { +Effect.ShowLoadingError }
            }
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> commands { +Command.GetMapMarkers }
            is Ui.Action.SelectTab -> state { copy(selectedTab = event.tab) }
            is Ui.Action.OnListMarkerSelected -> effects {
                +Effect.AnimateCameraToPlace(
                    mapMarker = event.mapMarker,
                    collapseBottomSheet = state.appSettings.autoHideBottomSheet,
                )
            }

            is Ui.Action.Reload -> {
                state { copy(loadingError = null) }
                commands { +Command.GetMapMarkers }
            }
        }
    }
}
