package kekmech.ru.feature_debug_menu.presentation.screens.main.elm

import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuEvent.Internal
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuEvent.Ui
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuCommand as Command
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuEffect as Effect
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuEvent as Event
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuState as State

internal class DebugMenuReducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
    uiEventClass = Ui::class,
    internalEventClass = Internal::class,
) {

    override fun Result.internal(event: Internal): Any? {
        TODO("Not yet implemented")
    }

    override fun Result.ui(event: Ui): Any? {
        TODO("Not yet implemented")
    }
}
