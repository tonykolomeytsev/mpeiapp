package kekmech.ru.feature_debug_menu.presentation.screens.main.elm

import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuCommand
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuEvent
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuCommand as Command
import kekmech.ru.feature_debug_menu.presentation.screens.main.elm.DebugMenuEvent as Event

internal class DebugMenuActor : Actor<Command, Event> {

    override fun execute(command: DebugMenuCommand): Flow<DebugMenuEvent> {
        TODO("Not yet implemented")
    }
}
