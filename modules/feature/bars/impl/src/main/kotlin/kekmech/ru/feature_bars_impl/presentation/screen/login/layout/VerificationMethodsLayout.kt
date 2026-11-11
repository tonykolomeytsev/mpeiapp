package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.VerificationKind
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethod
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethodId
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBarNavigationIconScope.CloseIconButton
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val DISABLED_ALPHA = 0.38f

@Composable
internal fun VerificationMethodsLayout(
    step: LoginStep.ChooseVerificationMethod,
    login: LoginState,
    actions: BarsLoginActions,
) {
    // The state doesn't say which method the code is being sent to, so the tapped one is kept here
    var chosenId by rememberSaveable { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIconButton(actions::onBack) },
                actions = { CloseIconButton(actions::onClose) },
            )
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .adaptiveContentWidth()
                .padding(top = 8.dp, bottom = 16.dp),
        ) {
            Text(
                text = "Способ подтверждения",
                style = MpeixTheme.typography.headlineBig,
                color = MpeixTheme.palette.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Text(
                text = "Куда отправить код для входа?",
                style = MpeixTheme.typography.bodyLarge,
                color = MpeixTheme.palette.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            step.methods.forEach { method ->
                val isChosen = login.isWorking && method.id.value == chosenId
                ListItem(
                    headlineText = method.label,
                    leadingContent = { Icon(method.kind.icon()) },
                    trailingContent = if (isChosen) {
                        {
                            CircularProgressIndicator(
                                color = MpeixTheme.palette.primary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier
                        .alpha(if (login.isWorking && !isChosen) DISABLED_ALPHA else 1f)
                        .clickable(enabled = !login.isWorking) {
                            chosenId = method.id.value
                            actions.onChooseVerificationMethod(method.id)
                        },
                )
            }
            login.error?.let { error ->
                Text(
                    text = error.text(),
                    style = MpeixTheme.typography.bodyMedium,
                    color = MpeixTheme.palette.error,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

private fun VerificationKind.icon(): ImageVector =
    when (this) {
        VerificationKind.Totp,
        VerificationKind.TemporaryCode -> Icons.Outlined.Lock
        // Telegram, VK, MAX and messengers the SDK doesn't know yet
        else -> Icons.Outlined.Send
    }

@Preview
@Composable
private fun VerificationMethodsPreview() {
    val step = LoginStep.ChooseVerificationMethod(
        listOf(
            VerificationMethod(VerificationMethodId("3"), VerificationKind.Max, "МАКС"),
            VerificationMethod(VerificationMethodId("1"), VerificationKind.Telegram, "Telegram"),
            VerificationMethod(VerificationMethodId("2"), VerificationKind.Vk, "ВКонтакте"),
        )
    )
    VerificationMethodsLayout(
        step = step,
        login = previewLoginState(step),
        actions = NoOpActions,
    )
}
