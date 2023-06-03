package kekmech.ru.feature_map.screens.main.elm

import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.feature_map.screens.main.elm.MapEvent.Internal
import kekmech.ru.feature_map.screens.main.elm.MapEvent.Ui
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import java.util.UUID
import kekmech.ru.feature_map.screens.main.elm.MapCommand as Command
import kekmech.ru.feature_map.screens.main.elm.MapEffect as Effect
import kekmech.ru.feature_map.screens.main.elm.MapEvent as Event
import kekmech.ru.feature_map.screens.main.elm.MapState as State

internal class MapReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {

    override fun Result.internal(event: Internal): Any =
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

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> commands { +Command.GetMapMarkers }
            is Ui.Action.OnMapReady -> {
                state { copy(map = event.map) }
                effects {
                    +Effect.GenerateGoogleMapMarkers(
                        map = event.map,
                        markers = state.markers,
                        googleMapMarkers = state.googleMapMarkers,
                        selectedTab = state.selectedTab
                    )
                }
            }
            is Ui.Action.SelectTab -> {
                state { copy(selectedTab = event.tab) }
                effects {
                    +Effect.GenerateGoogleMapMarkers(
                        state.map,
                        state.markers,
                        state.googleMapMarkers,
                        event.tab
                    )
                }
            }
            is Ui.Action.BottomSheetStateChanged -> state {
                copy(bottomSheetState = event.newState)
            }
            is Ui.Action.GoogleMapMarkersGenerated -> state {
                copy(googleMapMarkers = event.googleMapMarkers)
            }
            is Ui.Action.OnListMarkerSelected -> effects {
                +state.map?.let { map ->
                    Effect.AnimateCameraToPlace(
                        map = map,
                        googleMapMarkers = state.googleMapMarkers,
                        mapMarker = event.mapMarker,
                        collapseBottomSheet = state.appSettings.autoHideBottomSheet
                    )
                }
            }
            is Ui.Action.SilentUpdate -> state { copy(hash = UUID.randomUUID().toString()) }
            is Ui.Action.Reload -> {
                state { copy(loadingError = null) }
                commands { +Command.GetMapMarkers }
            }
            is Ui.Action.ScrollToTop -> state {
                copy(bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
}
