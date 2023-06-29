package kekmech.ru.debug_menu.presentation.screens.main.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuCommand
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent.Internal
import kekmech.ru.domain_app_settings.AppEnvironmentRepository
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuCommand as Command
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent as Event

internal class DebugMenuActor(
    private val appEnvironmentRepository: AppEnvironmentRepository,
) : Actor<Command, Event> {

    override fun execute(command: DebugMenuCommand): Flow<DebugMenuEvent> =
        when (command) {
            is Command.GetAppEnvironment -> actorFlow {
                appEnvironmentRepository.getAppEnvironment()
            }.mapEvents({ Internal.GetAppEnvironmentSuccess(it) })
            is Command.ChangeAppEnvironment -> actorFlow {
                appEnvironmentRepository.setAppEnvironment(command.appEnvironment)
            }.mapEvents({ Internal.ChangeAppEnvironmentSuccess(command.appEnvironment) })
        }
}
