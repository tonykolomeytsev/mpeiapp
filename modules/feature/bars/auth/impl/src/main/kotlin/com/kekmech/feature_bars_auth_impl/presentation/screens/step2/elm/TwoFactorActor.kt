package com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm

import com.kekmech.feature_bars_auth_impl.data.AuthRepository
import com.kekmech.feature_bars_auth_impl.domain.InternalAuthState
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.take
import money.vivid.elmslie.core.store.Actor
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent as Event

private const val TWO_FACTOR_DEBOUNCE_SEC = 30

internal class TwoFactorActor(
    private val authRepository: AuthRepository,
) : Actor<Command, Event>() {
    override fun execute(command: Command): Flow<Event> = when (command) {
        is Command.GetAuth2faProviders -> authRepository.subscribeAuthState()
            .mapNotNull {
                when (it) {
                    is InternalAuthState.Awaiting2faProviderSelection -> it.availableProviders
                    is InternalAuthState.Awaiting2faSubmission -> it.availableProviders
                    else -> null
                }
            }
            .take(1)
            .mapEvents(Internal::GetAuth2faProvidersSuccess)

        is Command.SubscribeTwoFactorCodeTimer -> flow {
            for (i in TWO_FACTOR_DEBOUNCE_SEC downTo 0) {
                emit(i)
                delay(1000)
            }
        }.mapEvents(Internal::SubscribeTwoFactorCodeTimerSuccess)

        is Command.RequestTwoFactorCode -> actorFlow {
            authRepository.request2faCode(command.provider).fold(
                ifLeft = Internal::RequestTwoFactorCodeFailure,
                ifRight = {
                    Internal.RequestTwoFactorCodeSuccess(
                        provider = command.provider,
                        debounceSec = TWO_FACTOR_DEBOUNCE_SEC,
                    )
                }
            )
        }

        is Command.Submit2faCode -> actorFlow {
            authRepository.submit2faCode(command.code).fold(
                ifLeft = Internal::Submit2faCodeFailure,
                ifRight = { Internal.Submit2faCodeSuccess },
            )
        }
    }
}