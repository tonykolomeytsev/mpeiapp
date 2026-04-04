package com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm

import com.kekmech.feature_bars_auth_api.AccountAutoSelectionService
import com.kekmech.feature_bars_auth_impl.data.AuthRepository
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext
import money.vivid.elmslie.core.store.Actor
import kotlin.time.Duration.Companion.seconds
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent as Event

internal class BarsAuthMainActor(
    private val authRepository: AuthRepository,
    private val router: Router,
    private val accountAutoSelectionService: AccountAutoSelectionService,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> = when (command) {
        is Command.SubscribeInternalAuthState -> authRepository.subscribeAuthState()
            .zip(authRepository.subscribeFatalError(), ::Pair)
            .mapEvents(
                eventMapper = { (state, fatal) ->
                    Internal.SubscribeInternalAuthStateSuccess(state, fatal)
                },
            )

        is Command.CloseLoginFlow -> actorFlow {
            withContext(Dispatchers.Main) {
                router.executeCommand(PopBackStack())
            }
        }.mapEvents()

        is Command.SelectAccountAutomatically -> actorFlow {
            if (command.afterRetry) {
                delay(3.seconds)
            }
            accountAutoSelectionService.invoke().fold(
                ifLeft = Internal::SelectAccountAutomaticallyFailure,
                ifRight = { Internal.SelectAccountAutomaticallySuccess },
            )
        }
    }
}