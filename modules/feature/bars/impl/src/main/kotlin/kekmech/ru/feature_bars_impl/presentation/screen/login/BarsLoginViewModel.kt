package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.tonykolomeytsev.mpeix.sdk.Account
import io.github.tonykolomeytsev.mpeix.sdk.AccountId
import io.github.tonykolomeytsev.mpeix.sdk.LoginAttempt
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethodId
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val SUCCESS_DISPLAY_MILLIS = 2000L

internal sealed interface BarsLoginUiState {
    data object Loading : BarsLoginUiState

    /** The first layout of the sheet: known accounts and "Add account". */
    data class Accounts(val accounts: List<Account>) : BarsLoginUiState

    /** The SDK login state machine. The sheet renders whatever step it gets. */
    data class Attempt(val login: LoginState) : BarsLoginUiState
}

internal interface BarsLoginActions {
    fun onAddAccount()
    fun onAccountClick(id: AccountId)
    fun onBack()
    fun onSubmitUsername(username: String)
    fun onSubmitPassword(password: String)
    fun onChooseVerificationMethod(id: VerificationMethodId)
    fun onSubmitCode(code: String)
    fun onResendCode()
    fun onChooseSubaccount(id: SubaccountId)
}

/**
 * Login bottom sheet: account list, then one [LoginAttempt] of the SDK (docs/DESIGN.md, "Login state machine").
 *
 * The attempt is not cancelled when the sheet is closed: `resumeLogin` returns the live attempt
 * for ~3 minutes, so the user can close the sheet while waiting for the code and come back.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class BarsLoginViewModel(
    private val sdk: MpeixSdk,
    private val barsProfileIdRepository: BarsProfileIdRepository,
    private val router: Router,
) : ViewModel(), BarsLoginActions {

    private val attempt = MutableStateFlow<LoginAttempt?>(null)
    private var completionJob: Job? = null

    val state: StateFlow<BarsLoginUiState> = attempt
        .flatMapLatest { attempt ->
            attempt?.state?.map(BarsLoginUiState::Attempt)
                ?: sdk.bars.auth.accounts.map(BarsLoginUiState::Accounts)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, BarsLoginUiState.Loading)

    override fun onAddAccount() {
        startAttempt(sdk.bars.auth.startLogin())
    }

    override fun onAccountClick(id: AccountId) {
        startAttempt(sdk.bars.auth.resumeLogin(id))
    }

    override fun onBack() {
        val attempt = attempt.value ?: return
        when (attempt.state.value.step) {
            // The first step of the attempt and the terminal error go back to the account list
            is LoginStep.EnterUsername,
            is LoginStep.Aborted -> {
                attempt.cancel()
                this.attempt.value = null
            }

            is LoginStep.Completed -> Unit
            else -> viewModelScope.launch { attempt.back() }
        }
    }

    override fun onSubmitUsername(username: String) = act { submitUsername(username.trim()) }

    override fun onSubmitPassword(password: String) = act { submitPassword(password) }

    override fun onChooseVerificationMethod(id: VerificationMethodId) = act { chooseVerificationMethod(id) }

    override fun onSubmitCode(code: String) = act { submitCode(code.trim()) }

    override fun onResendCode() = act { resendCode() }

    override fun onChooseSubaccount(id: SubaccountId) = act { chooseSubaccount(id) }

    private fun startAttempt(attempt: LoginAttempt) {
        this.attempt.value = attempt
        completionJob?.cancel()
        completionJob = viewModelScope.launch {
            val completed = attempt.state
                .map { it.step }
                .filterIsInstance<LoginStep.Completed>()
                .first()
            barsProfileIdRepository.saveCurrentProfileId(completed.profileId)
            delay(SUCCESS_DISPLAY_MILLIS.milliseconds)
            router.executeCommand(PopBackStack())
        }
    }

    private fun act(action: suspend LoginAttempt.() -> Unit) {
        val attempt = attempt.value ?: return
        viewModelScope.launch { attempt.action() }
    }
}
