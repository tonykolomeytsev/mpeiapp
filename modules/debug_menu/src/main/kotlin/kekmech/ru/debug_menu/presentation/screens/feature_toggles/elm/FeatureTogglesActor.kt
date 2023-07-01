package kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.common_feature_toggles.BooleanRemoteVariable
import kekmech.ru.common_feature_toggles.RemoteVariable
import kekmech.ru.common_feature_toggles.RewriteRemoteVariableHandle
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent.Internal
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesCommand as Command
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent as Event

internal class FeatureTogglesActor(
    private val remoteVariables: List<RemoteVariable<*>>,
    private val rewriteRemoteVariableHandle: RewriteRemoteVariableHandle,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.GetFeatureToggles -> actorFlow {
                remoteVariables
                    .filterIsInstance<BooleanRemoteVariable>()
                    .map {
                        FeatureToggle(
                            name = it.name,
                            value = it.value,
                            overwritten = rewriteRemoteVariableHandle.isRewritten(it.name),
                        )
                    }
            }.mapEvents(Internal::GetFeatureTogglesSuccess)
            is Command.RewriteFeatureToggle -> actorFlow {
                rewriteRemoteVariableHandle.override(
                    name = command.name,
                    value = command.value.toString(),
                )
                rewriteRemoteVariableHandle.isRewritten(command.name)
            }.mapEvents({
                Internal.RewriteFeatureToggleSuccess(
                    name = command.name,
                    value = command.value,
                    overwritten = it,
                )
            })
        }
}
