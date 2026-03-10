package kekmech.ru.feature_bars_impl.presentation.screen.login.elm

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import kekmech.ru.feature_bars_impl.presentation.screen.login.compose.AccountItemUi
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEffect as Effect
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent as Event
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginState as State

internal class BarsLoginReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {

    override fun Result.internal(event: Internal) =
        when (event) {
            is Internal.CheckAuthStatusSuccess -> {
                val stage = when (event.status) {
                    is AuthStatus.LoginRequired -> BarsLoginStage.LOGIN_PASSWORD
                    is AuthStatus.AccountSelectionRequired,
                    is AuthStatus.LoggedIn -> BarsLoginStage.ACCOUNT_SELECTION
                }
                state { copy(stage = stage) }
                if (stage == BarsLoginStage.ACCOUNT_SELECTION) {
                    handleAccountSelection()
                }
                Unit
            }

            is Internal.CheckAuthStatusFailure -> {
                // Should never fail in this version of lib_bars due to lack of network activity (nothing to fall)
                // Will fix this state in future releases
            }

            is Internal.LoginWithPasswordSuccess -> {
                loginPasswordState { copy(failure = null) }
                when (val status = event.status) {
                    is LoginStatus.WrongCredentials -> {
                        loginPasswordState { copy(isLoading = false) }
                        effects { +Effect.ShowInvalidCredsText }
                    }

                    is LoginStatus.AccountSelectionRequired -> handleAccountSelection()

                    is LoginStatus.TwoFactorRequired -> {
                        twoFactorCodeState {
                            copy(
                                providers = status.providers,
                                codeState = CodeState.SendingCode(status.defaultProvider),
                                isLoading = true,
                            )
                        }
                        state { copy(stage = BarsLoginStage.TWO_FACTOR_CODE) }
                        commands { +Command.RequestTwoFactorCode(status.defaultProvider) }
                    }
                }
            }

            is Internal.LoginWithPasswordFailure -> {
                loginPasswordState { copy(failure = event.throwable) }
            }

            is Internal.RequestTwoFactorCodeSuccess -> {
                commands { +Command.SubscribeTwoFactorCodeTimer }
                twoFactorCodeState {
                    copy(
                        codeState = CodeState.CodeSent(
                            provider = event.provider,
                            resendDebounceSec = 30,
                        ),
                        isLoading = false,
                        failure = null,
                    )
                }
            }

            is Internal.SubscribeTwoFactorCodeTimerSuccess -> {
                val codeSent = state.twoFactorCodeState.codeState as? CodeState.CodeSent ?: return
                twoFactorCodeState {
                    copy(
                        codeState = codeSent.copy(
                            resendDebounceSec = event.secRemains,
                        )
                    )
                }
            }

            is Internal.RequestTwoFactorCodeFailure -> {
                twoFactorCodeState { copy(failure = event.throwable) }
            }

            is Internal.Submit2faCodeSuccess -> {
                twoFactorCodeState { copy(failure = null) }
                when (event.status) {
                    TwoFactorCodeStatus.InvalidCode -> {
                        effects { +Effect.ShowInvalidCodeText }
                    }

                    TwoFactorCodeStatus.AccountSelectionRequired -> handleAccountSelection()
                }
            }

            is Internal.Submit2faCodeFailure -> {
                twoFactorCodeState { copy(failure = event.throwable) }
            }

            is Internal.GetAccountsSuccess -> {
                accountSelectionState {
                    copy(
                        accounts = event.accounts.map { it.toUi() },
                        selectedAccountId = event.accounts.find {
                            it.status.contains(
                                "обучается",
                                ignoreCase = true
                            )
                        }?.id,
                        isLoading = false,
                        failure = null,
                    )
                }
            }

            is Internal.GetAccountsFailure -> {
                accountSelectionState { copy(failure = event.throwable) }
            }
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> {
                commands { +Command.CheckAuthStatus }
            }

            is Ui.Click.Back -> {
                commands { +Command.Exit }
            }

            is Ui.Click.SubmitLoginPassword -> {
                loginPasswordState {
                    copy(
                        login = event.login,
                        password = event.password,
                        isLoading = true,
                    )
                }
                commands { +Command.LoginWithPassword(event.login, event.password) }
            }

            is Ui.Click.Resend -> {
                twoFactorCodeState {
                    copy(
                        codeState = CodeState.SendingCode(event.provider),
                        isLoading = true,
                    )
                }
                commands { +Command.RequestTwoFactorCode(event.provider) }
            }

            is Ui.Click.SubmitCode -> {
                twoFactorCodeState { copy(isLoading = true) }
                commands { +Command.Submit2faCode(event.code, state.loginPasswordState.login) }
            }

            is Ui.Click.AvailableAccount -> {
                accountSelectionState {
                    copy(selectedAccountId = event.accountId)
                }
            }

            is Ui.Click.SubmitAccountName -> {
                val accId = state.accountSelectionState.selectedAccountId ?: return
                commands { +Command.SubmitAccountId(accId) }
                accountSelectionState { copy(isLoading = true) }
            }
        }

    private fun Result.handleAccountSelection() {
        state {
            copy(stage = BarsLoginStage.ACCOUNT_SELECTION)
        }
        accountSelectionState {
            copy(isLoading = true)
        }
        commands { +Command.GetAccounts }
    }

    private inline fun Result.loginPasswordState(
        crossinline update: BarsLoginPasswordState.() -> BarsLoginPasswordState,
    ) {
        state { copy(loginPasswordState = loginPasswordState.update()) }
    }

    private inline fun Result.twoFactorCodeState(
        crossinline update: BarsTwoFactorCodeState.() -> BarsTwoFactorCodeState,
    ) {
        state { copy(twoFactorCodeState = twoFactorCodeState.update()) }
    }

    private inline fun Result.accountSelectionState(
        crossinline update: BarsAccountSelectionState.() -> BarsAccountSelectionState,
    ) {
        state { copy(accountSelectionState = accountSelectionState.update()) }
    }

    private fun Account.toUi(): AccountItemUi =
        AccountItemUi(
            id = id,
            name = name,
            group = group,
            status = status.capitalize(LocaleList.current)
        )
}