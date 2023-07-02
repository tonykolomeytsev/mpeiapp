package kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm

import kekmech.ru.common_feature_toggles.BooleanRemoteVariable
import kekmech.ru.common_feature_toggles.RemoteVariable
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.FeatureTogglesOverwriteMiddleware
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent.Internal
import kekmech.ru.library_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesCommand as Command
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent as Event

internal class FeatureTogglesActor(
    private val remoteVariables: List<RemoteVariable<*>>,
    private val overwriteMiddleware: FeatureTogglesOverwriteMiddleware,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.GetFeatureToggles -> actorFlow {
                getFeatureToggles()
            }.mapEvents(Internal::GetFeatureTogglesSuccess)

            is Command.OverwriteFeatureToggle -> actorFlow {
                overwriteMiddleware.overwrite(
                    name = command.name,
                    value = command.value.toString(),
                )
                overwriteMiddleware.isOverwritten(command.name)
            }.mapEvents({
                Internal.OverwriteFeatureToggleSuccess(
                    name = command.name,
                    value = command.value,
                    overwritten = it,
                )
            })

            is Command.ResetFeatureToggles -> actorFlow {
                overwriteMiddleware.reset()
                getFeatureToggles()
            }.mapEvents(Internal::ResetFeatureTogglesSuccess)
        }

    private fun getFeatureToggles(): List<FeatureToggle> = remoteVariables
        .filterIsInstance<BooleanRemoteVariable>()
        .map {
            FeatureToggle(
                name = it.name,
                value = it.value,
                overwritten = overwriteMiddleware.isOverwritten(it.name),
            )
        }
}
