package com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm

import com.kekmech.feature_bars_auth_api.SelectAccountError
import com.kekmech.feature_bars_auth_impl.domain.InternalAuthState
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent.Internal
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainState as State

internal class BarsAuthMainReducer : ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
    uiEventClass = Ui::class,
    internalEventClass = Internal::class,
) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SubscribeInternalAuthStateSuccess -> {
            val newStage = if (event.hasFatalError) {
                AuthStage.IrrecoverableError
            } else when (event.state) {
                is InternalAuthState.Idle -> AuthStage.Loading

                is InternalAuthState.LoggedIn -> {
                    commands { +Command.CloseLoginFlow }
                    AuthStage.Loading
                }

                is InternalAuthState.AwaitingAccountSelection -> {
                    commands { +Command.SelectAccountAutomatically(afterRetry = false) }
                    AuthStage.Loading
                }

                is InternalAuthState.LoggedOut,
                is InternalAuthState.SessionExpired,
                is InternalAuthState.VerifyingCredentials -> AuthStage.LoginPassword

                is InternalAuthState.Awaiting2faProviderSelection,
                is InternalAuthState.RequestingTwoFactorCode,
                is InternalAuthState.Awaiting2faSubmission,
                is InternalAuthState.SubmittingTwoFactorCode -> AuthStage.TwoFactor

            }
            state { copy(stage = newStage) }
        }

        is Internal.SelectAccountAutomaticallyFailure -> {
            val newStage = when (event.selectAccountError) {
                is SelectAccountError.Network -> {
                    commands { +Command.SelectAccountAutomatically(afterRetry = true) }
                    AuthStage.Loading
                }

                else -> AuthStage.IrrecoverableError
            }
            state { copy(stage = newStage) }
        }

        is Internal.SelectAccountAutomaticallySuccess -> {
            commands { +Command.CloseLoginFlow }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {
            commands { +Command.SubscribeInternalAuthState }
        }
    }
}