package com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm

import com.kekmech.feature_bars_auth_impl.domain.TwoFactorRequestError
import com.kekmech.feature_bars_auth_impl.domain.TwoFactorSubmitError
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent.Internal
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorState as State

internal class TwoFactorReducer : ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
    uiEventClass = Ui::class,
    internalEventClass = Internal::class,
) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.GetAuth2faProvidersSuccess -> {
            val defaultProvider = event.providers.find { it.default } ?: event.providers.random()
            state {
                copy(
                    providers = event.providers,
                    codeState = CodeState.SendingCode(defaultProvider),
                )
            }
            commands { +Command.RequestTwoFactorCode(defaultProvider) }
        }

        is Internal.SubscribeTwoFactorCodeTimerSuccess -> {
            state { copy(debounceSec = event.secRemains) }
        }

        is Internal.RequestTwoFactorCodeSuccess -> {
            state {
                copy(
                    codeState = CodeState.CodeSent(provider = event.provider),
                    debounceSec = event.debounceSec,
                    isLoading = false,
                )
            }
            commands { +Command.SubscribeTwoFactorCodeTimer }
        }

        is Internal.RequestTwoFactorCodeFailure -> {
            state {
                copy(
                    codeState = CodeState.Initial,
                    debounceSec = 0,
                )
            }
            when (val error = event.error) {
                is TwoFactorRequestError.Network -> effects { +Effect.ShowNetworkErrorBanner }
                is TwoFactorRequestError.Fatal -> effects { +Effect.ShowFatalErrorBanner }
                is TwoFactorRequestError.Message -> effects { +Effect.ShowServerErrorBanner(error.message) }
            }
        }

        is Internal.Submit2faCodeSuccess -> {
            state { copy(isLoading = false) }
        }

        is Internal.Submit2faCodeFailure -> {
            state {
                copy(
                    codeState = CodeState.Initial,
                    isLoading = false,
                )
            }
            when (event.error) {
                TwoFactorSubmitError.Fatal -> effects { +Effect.ShowFatalErrorBanner }
                TwoFactorSubmitError.Network -> effects { +Effect.ShowNetworkErrorBanner }
                TwoFactorSubmitError.WrongCode -> effects { +Effect.ShowInvalidCodeAlert }
            }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {
            commands { +Command.GetAuth2faProviders }
        }

        is Ui.Action.ResendCode -> {
            state { copy(codeState = CodeState.SendingCode(event.provider)) }
            commands { +Command.RequestTwoFactorCode(event.provider) }
        }

        is Ui.Action.SubmitCode -> {
            state { copy(isLoading = true) }
            commands { +Command.Submit2faCode(event.code) }
        }
    }
}