package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun AbortedLayout(
    step: LoginStep.Aborted,
    actions: BarsLoginActions,
) {
    Scaffold(
        // Back closes the screen after the error, so there is no cross
        topBar = { TopAppBar(navigationIcon = { BackIconButton(actions::onBack) }) },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Spacer(Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.adaptiveContentWidth(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = MpeixTheme.palette.error,
                    modifier = Modifier.size(72.dp),
                )
                Text(
                    text = "Не удалось войти",
                    style = MpeixTheme.typography.titleLarge,
                    color = MpeixTheme.palette.onSurface,
                )
                Text(
                    text = step.reason.text(),
                    style = MpeixTheme.typography.bodyLarge,
                    color = MpeixTheme.palette.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            Button(
                text = "Попробовать снова",
                onClick = actions::onRetry,
                modifier = Modifier
                    .adaptiveContentWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun AbortedPreview() {
    MpeixTheme {
        AbortedLayout(
            step = LoginStep.Aborted(LoginError.TooManyAttempts(retryAt = null)),
            actions = NoOpActions,
        )
    }
}
