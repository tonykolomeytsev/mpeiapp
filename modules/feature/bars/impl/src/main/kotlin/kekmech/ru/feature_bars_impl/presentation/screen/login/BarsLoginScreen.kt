package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.HideKeyboardOnPause
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.AbortedLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.CodeLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.CompletedLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.CredentialsLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.SubaccountsLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.VerificationMethodsLayout

/**
 * Login screen. Every step is a separate screen with its own Scaffold.
 * Layouts of the steps are in the `layout` package.
 */
@Composable
internal fun BarsLoginScreen(
    state: LoginState,
    actions: BarsLoginActions,
) {
    HideKeyboardOnPause()
    // On the first step and after the error system back closes the screen as usual,
    // in the middle of the attempt it goes to the previous step
    BackHandler(enabled = state.step !is LoginStep.EnterCredentials && state.step !is LoginStep.Aborted) {
        actions.onBack()
    }

    when (val step = state.step) {
        is LoginStep.EnterCredentials -> CredentialsLayout(step, state, actions)
        is LoginStep.ChooseVerificationMethod -> VerificationMethodsLayout(step, state, actions)
        is LoginStep.EnterCode -> CodeLayout(step, state, actions)
        is LoginStep.ChooseSubaccount -> SubaccountsLayout(step, state, actions)
        is LoginStep.Completed -> CompletedLayout()
        is LoginStep.Aborted -> AbortedLayout(step, actions)
    }
}
