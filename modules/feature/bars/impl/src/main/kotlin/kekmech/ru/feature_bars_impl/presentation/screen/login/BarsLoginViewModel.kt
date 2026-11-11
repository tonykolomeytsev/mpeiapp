package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.tonykolomeytsev.mpeix.sdk.AccountId
import io.github.tonykolomeytsev.mpeix.sdk.LoginAttempt
import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethodId
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.navigation.BarsLoginScreenKey
import kekmech.ru.feature_bars_impl.navigation.CloseBarsLogin
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.TOO_MANY_CODES_MESSAGE
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val SUCCESS_DISPLAY_MILLIS = 2000L

/** Wrong codes allowed in one attempt. The site shows no limit, so the app sets one. */
internal const val MAX_CODE_ATTEMPTS = 3

internal interface BarsLoginActions {
    /** Back arrow and system back: the previous step, or closes the screen on the first one. */
    fun onBack()

    /** Closes the screen from any step. The attempt stays alive, so the account can resume it. */
    fun onClose()

    /** Starts the attempt again after [LoginStep.Aborted]. */
    fun onRetry()
    fun onSubmitCredentials(username: String, password: String)
    fun onChooseVerificationMethod(id: VerificationMethodId)
    fun onSubmitCode(code: String)
    fun onResendCode()
    fun onChooseSubaccount(id: SubaccountId)
}

/**
 * Login screen: one [LoginAttempt] of the SDK (docs/DESIGN.md, "Login state machine").
 *
 * The attempt is not cancelled when the screen is closed in the middle: `resumeLogin` returns the live attempt
 * for ~3 minutes, so the user can leave while waiting for the code and come back from the account list.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class BarsLoginViewModel(
    private val key: BarsLoginScreenKey,
    private val sdk: MpeixSdk,
    private val barsProfileIdRepository: BarsProfileIdRepository,
    private val router: Router,
) : ViewModel(), BarsLoginActions {

    private val attempt = MutableStateFlow(newAttempt())
    private var completionJob: Job? = null

    /** Wrong codes in the current attempt. At [MAX_CODE_ATTEMPTS] the attempt is cancelled. */
    private val wrongCodes = MutableStateFlow(0)

    val state: StateFlow<LoginState> = combine(attempt.flatMapLatest { it.state }, wrongCodes) { login, wrong ->
        if (wrong >= MAX_CODE_ATTEMPTS) TOO_MANY_CODES else login
    }.stateIn(viewModelScope, SharingStarted.Eagerly, attempt.value.state.value)

    init {
        watchCompletion(attempt.value)
    }

    override fun onBack() {
        val attempt = attempt.value
        // What the screen shows: the app's own "too many codes" state included
        val state = state.value
        when (state.step) {
            // The attempt is cancelled in onCleared
            is LoginStep.EnterCredentials,
            is LoginStep.Aborted -> router.executeCommand(PopBackStack())

            is LoginStep.Completed -> Unit
            // The SDK ignores actions while a request is running and reports them as programmer errors
            else -> if (!state.isWorking) viewModelScope.launch { attempt.back() }
        }
    }

    override fun onClose() {
        router.executeCommand(PopBackStack())
    }

    override fun onRetry() {
        attempt.value.cancel()
        wrongCodes.value = 0
        attempt.value = newAttempt().also(::watchCompletion)
    }

    override fun onSubmitCredentials(username: String, password: String) =
        act { submitCredentials(username.trim(), password) }

    override fun onChooseVerificationMethod(id: VerificationMethodId) = act { chooseVerificationMethod(id) }

    override fun onSubmitCode(code: String) = act {
        // `this` is the attempt: its own state, not the screen state
        val before = this.state.value
        submitCode(code.trim())
        // An ignored call (e.g. while a request runs) leaves the very same state: not a try
        val after = this.state.value
        if (after !== before && after.error == LoginError.InvalidCode) {
            wrongCodes.update { it + 1 }
            if (wrongCodes.value >= MAX_CODE_ATTEMPTS) this.cancel()
        }
    }

    override fun onResendCode() = act { resendCode() }

    override fun onChooseSubaccount(id: SubaccountId) = act { chooseSubaccount(id) }

    override fun onCleared() {
        // Nothing to keep on the first step and after the terminal error
        when (state.value.step) {
            is LoginStep.EnterCredentials,
            is LoginStep.Aborted -> attempt.value.cancel()

            else -> Unit
        }
    }

    private fun newAttempt(): LoginAttempt =
        key.accountId
            ?.let { sdk.bars.auth.resumeLogin(AccountId(it)) }
            ?: sdk.bars.auth.startLogin()

    private fun watchCompletion(attempt: LoginAttempt) {
        completionJob?.cancel()
        completionJob = viewModelScope.launch {
            val completed = attempt.state
                .map { it.step }
                .filterIsInstance<LoginStep.Completed>()
                .first()
            barsProfileIdRepository.saveCurrentProfileId(completed.profileId)
            delay(SUCCESS_DISPLAY_MILLIS.milliseconds)
            router.executeCommand(CloseBarsLogin())
        }
    }

    private fun act(action: suspend LoginAttempt.() -> Unit) {
        val attempt = attempt.value
        viewModelScope.launch { attempt.action() }
    }

    private companion object {
        val TOO_MANY_CODES = LoginState(LoginStep.Aborted(LoginError.Rejected(TOO_MANY_CODES_MESSAGE)), false, null)
    }
}
