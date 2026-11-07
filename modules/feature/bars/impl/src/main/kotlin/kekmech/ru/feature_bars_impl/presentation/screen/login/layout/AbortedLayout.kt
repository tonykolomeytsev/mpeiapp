package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun AbortedLayout(step: LoginStep.Aborted, actions: BarsLoginActions) {
    StatusLayout(
        icon = Icons.Outlined.Warning,
        iconTint = MpeixTheme.palette.error,
        title = "Не удалось войти",
        description = step.reason.text(),
    ) {
        PrimaryButton(text = "Попробовать снова", onClick = actions::onBack)
    }
}

@Preview
@Composable
private fun AbortedPreview() = LayoutPreview {
    AbortedLayout(
        step = LoginStep.Aborted(LoginError.TooManyAttempts(retryAt = null)),
        actions = NoOpActions,
    )
}
