package kekmech.ru.debug_menu.presentation.screens.main.elm

import kekmech.ru.common_kotlin.toResource
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent.Internal
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent.Ui
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuCommand as Command
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEffect as Effect
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent as Event
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuState as State

internal class DebugMenuReducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
    uiEventClass = Ui::class,
    internalEventClass = Internal::class,
) {

    override fun Result.internal(event: Internal) =
        when (event) {
            is Internal.GetAppEnvironmentSuccess ->
                state { copy(appEnvironment = event.appEnvironment.toResource()) }
            is Internal.SetAppEnvironmentSuccess -> {
                state { copy(appEnvironment = event.appEnvironment.toResource()) }
                effects { +Effect.ReloadApp }
            }
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> {
                commands { +Command.GetAppEnvironment }
            }
            is Ui.Click.Environment -> {
                commands { +Command.SetAppEnvironment(event.appEnvironment) }
            }
        }
}
