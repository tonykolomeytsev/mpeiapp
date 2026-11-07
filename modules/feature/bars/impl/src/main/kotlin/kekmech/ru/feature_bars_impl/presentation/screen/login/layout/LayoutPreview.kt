package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.tonykolomeytsev.mpeix.sdk.AccountId
import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethodId
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_theme.theme.MpeixTheme

// Helpers for the previews of the layouts in this package

internal object NoOpActions : BarsLoginActions {
    override fun onAddAccount() = Unit
    override fun onAccountClick(id: AccountId) = Unit
    override fun onBack() = Unit
    override fun onSubmitUsername(username: String) = Unit
    override fun onSubmitPassword(password: String) = Unit
    override fun onChooseVerificationMethod(id: VerificationMethodId) = Unit
    override fun onSubmitCode(code: String) = Unit
    override fun onResendCode() = Unit
    override fun onChooseSubaccount(id: SubaccountId) = Unit
}

/** Theme and the sheet background around a layout. */
@Composable
internal fun LayoutPreview(content: @Composable () -> Unit) {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            content()
        }
    }
}

internal fun previewLoginState(step: LoginStep, error: LoginError? = null, isWorking: Boolean = false) =
    LoginState(step = step, isWorking = isWorking, error = error)
