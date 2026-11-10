package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethodId
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions

// Helpers for the previews of the layouts in this package

internal object NoOpActions : BarsLoginActions {
    override fun onBack() = Unit
    override fun onClose() = Unit
    override fun onRetry() = Unit
    override fun onSubmitCredentials(username: String, password: String) = Unit
    override fun onChooseVerificationMethod(id: VerificationMethodId) = Unit
    override fun onSubmitCode(code: String) = Unit
    override fun onResendCode() = Unit
    override fun onChooseSubaccount(id: SubaccountId) = Unit
}

internal fun previewLoginState(step: LoginStep, error: LoginError? = null, isWorking: Boolean = false) =
    LoginState(step = step, isWorking = isWorking, error = error)
