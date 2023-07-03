package kekmech.ru.debug_menu.presentation.screens.main.elm

import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuCommand
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent.Internal
import kekmech.ru.feature_app_settings_api.data.AppEnvironmentRepository
import kekmech.ru.library_elm.actorFlow
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
            is Command.SetAppEnvironment -> actorFlow {
                appEnvironmentRepository.setAppEnvironment(command.appEnvironment)
            }.mapEvents({ Internal.SetAppEnvironmentSuccess(command.appEnvironment) })
        }
}
