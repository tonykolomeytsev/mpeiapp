package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.AbortedLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.AccountsLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.CodeLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.CompletedLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.PasswordLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.SubaccountsLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.UsernameLayout
import kekmech.ru.feature_bars_impl.presentation.screen.login.layout.VerificationMethodsLayout

/**
 * Content of the login bottom sheet. The sheet stays the same, only the layout inside it changes.
 * Layouts of the steps are in the `layout` package.
 */
@Composable
internal fun BarsLoginSheet(
    state: BarsLoginUiState,
    actions: BarsLoginActions,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state,
        contentKey = { it.layoutKey() },
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = modifier
            .fillMaxWidth()
            .imePadding(),
        label = "BarsLoginSheet",
    ) { state ->
        when (state) {
            is BarsLoginUiState.Loading -> Spacer(Modifier.height(240.dp))
            is BarsLoginUiState.Accounts -> AccountsLayout(state.accounts, actions)
            is BarsLoginUiState.Attempt -> AttemptLayout(state.login, actions)
        }
    }
}

/** Text fields keep their state while the step stays the same, so the key is the step type. */
private fun BarsLoginUiState.layoutKey(): Any =
    when (this) {
        is BarsLoginUiState.Attempt -> login.step::class
        else -> this::class
    }

@Composable
private fun AttemptLayout(login: LoginState, actions: BarsLoginActions) {
    when (val step = login.step) {
        is LoginStep.EnterUsername -> UsernameLayout(step, login, actions)
        is LoginStep.EnterPassword -> PasswordLayout(step, login, actions)
        is LoginStep.ChooseVerificationMethod -> VerificationMethodsLayout(step, login, actions)
        is LoginStep.EnterCode -> CodeLayout(step, login, actions)
        is LoginStep.ChooseSubaccount -> SubaccountsLayout(step, login, actions)
        is LoginStep.Completed -> CompletedLayout()
        is LoginStep.Aborted -> AbortedLayout(step, actions)
    }
}
