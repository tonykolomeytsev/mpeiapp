package kekmech.ru.feature_bars_impl.presentation.screen.login.elm

import kekmech.ru.feature_bars_impl.presentation.screen.login.compose.AccountItemUi

// region: STATE

internal data class BarsLoginState(
    val stage: BarsLoginStage = BarsLoginStage.INIT,
    val loginPasswordState: BarsLoginPasswordState = BarsLoginPasswordState(),
    val twoFactorCodeState: BarsTwoFactorCodeState = BarsTwoFactorCodeState(),
    val accountSelectionState: BarsAccountSelectionState = BarsAccountSelectionState(),
)

internal data class BarsLoginPasswordState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val failure: Throwable? = null,
)

internal data class BarsTwoFactorCodeState(
    val codeState: CodeState = CodeState.Initial,
    val providers: List<CodeProvider> = emptyList(),
    val isLoading: Boolean = false,
    val failure: Throwable? = null,
)

internal sealed interface CodeState {
    data object Initial : CodeState
    data class SendingCode(val provider: CodeProvider) : CodeState
    data class CodeSent(
        val provider: CodeProvider,
        val resendDebounceSec: Int,
    ) : CodeState
}

internal enum class CodeProvider { MAX, VK, TG }

internal data class BarsAccountSelectionState(
    val selectedAccountId: String? = null,
    val accounts: List<AccountItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val failure: Throwable? = null,
)

internal enum class BarsLoginStage {
    INIT,
    LOGIN_PASSWORD,
    TWO_FACTOR_CODE,
    ACCOUNT_SELECTION,
}

// endregion: STATE

internal sealed interface AuthStatus {
    data object LoginRequired : AuthStatus
    data object AccountSelectionRequired : AuthStatus
    data object LoggedIn : AuthStatus
}

internal sealed interface LoginStatus {
    data object WrongCredentials : LoginStatus

    data class TwoFactorRequired(
        val defaultProvider: CodeProvider,
        val providers: List<CodeProvider>,
    ) : LoginStatus

    data object AccountSelectionRequired : LoginStatus
}

internal sealed interface TwoFactorCodeStatus {
    data object InvalidCode : TwoFactorCodeStatus
    data object AccountSelectionRequired : TwoFactorCodeStatus
}

internal data class Account(
    val id: String,
    val name: String,
    val group: String,
    val status: String,
)

internal sealed interface BarsLoginEvent {
    sealed interface Ui : BarsLoginEvent {
        data object Init : Ui

        sealed interface Click : Ui {
            data object Back : Click
            data class SubmitLoginPassword(val login: String, val password: String) : Click
            data class SubmitCode(val code: String) : Click
            data object SubmitAccountName : Click
            data class AvailableAccount(val accountId: String) : Click
            data class Resend(val provider: CodeProvider) : Click
        }
    }

    sealed interface Internal : BarsLoginEvent {
        // region: Init
        data class CheckAuthStatusSuccess(val status: AuthStatus) : Internal
        data class CheckAuthStatusFailure(val throwable: Throwable) : Internal
        // endregion: Init

        // region: Login w Pass
        data class LoginWithPasswordSuccess(val status: LoginStatus) : Internal
        data class LoginWithPasswordFailure(val throwable: Throwable) : Internal
        // endregion: Login w Pass

        // region: Request 2fa code
        data class RequestTwoFactorCodeSuccess(
            val provider: CodeProvider,
            val debounceSec: Int,
        ) : Internal

        data class RequestTwoFactorCodeFailure(val throwable: Throwable) : Internal
        data class SubscribeTwoFactorCodeTimerSuccess(val secRemains: Int) : Internal
        // endregion: Request 2fa code

        // region: Submit 2fa code
        data class Submit2faCodeSuccess(val status: TwoFactorCodeStatus) : Internal
        data class Submit2faCodeFailure(val throwable: Throwable) : Internal
        // endregion: Submit 2fa code

        // region: Accounts
        data class GetAccountsSuccess(val accounts: List<Account>) : Internal
        data class GetAccountsFailure(val throwable: Throwable) : Internal
        // endregion: Accounts
    }
}

internal sealed interface BarsLoginCommand {
    data object CheckAuthStatus : BarsLoginCommand
    data object Exit : BarsLoginCommand
    data class LoginWithPassword(val login: String, val password: String) : BarsLoginCommand
    data class RequestTwoFactorCode(val provider: CodeProvider) : BarsLoginCommand
    data object SubscribeTwoFactorCodeTimer : BarsLoginCommand
    data class Submit2faCode(val code: String, val login: String) : BarsLoginCommand
    data object GetAccounts : BarsLoginCommand
    data class SubmitAccountId(val id: String) : BarsLoginCommand
}

internal sealed interface BarsLoginEffect {
    data object ShowInvalidCodeText : BarsLoginEffect
    data object ShowInvalidCredsText : BarsLoginEffect
}
