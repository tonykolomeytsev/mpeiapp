package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.Resend
import io.github.tonykolomeytsev.mpeix.sdk.VerificationKind
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethod
import io.github.tonykolomeytsev.mpeix.sdk.VerificationMethodId
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonState
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBarNavigationIconScope.CloseIconButton
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
internal fun CodeLayout(
    step: LoginStep.EnterCode,
    login: LoginState,
    actions: BarsLoginActions,
) {
    var code by rememberSaveable { mutableStateOf("") }
    val submit = { if (code.isNotBlank()) actions.onSubmitCode(code) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIconButton(actions::onBack) },
                actions = { CloseIconButton(actions::onClose) },
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .adaptiveContentWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
            ) {
                Text(
                    text = "Введите код",
                    style = MpeixTheme.typography.headlineBig,
                    color = MpeixTheme.palette.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Text(
                    text = step.method.destinationText(),
                    style = MpeixTheme.typography.bodyLarge,
                    color = MpeixTheme.palette.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                OutlinedTextField(
                    value = code,
                    // Any length: the site may change it
                    onValueChange = { value -> code = value },
                    label = { Text("Код") },
                    singleLine = true,
                    enabled = !login.isWorking,
                    isError = login.error != null,
                    supportingText = login.error?.let { error ->
                        {
                            Text(
                                text = error.text(),
                                style = MpeixTheme.typography.bodyMedium,
                                color = MpeixTheme.palette.error,
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .focusRequester(rememberAutoFocus()),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp),
                ) {
                    when (val resend = step.resend) {
                        is Resend.AvailableAt ->
                            ResendButton(
                                resend,
                                isWorking = login.isWorking,
                                onClick = actions::onResendCode
                            )

                        is Resend.Unsupported -> Unit
                    }
                    Spacer(Modifier.weight(1f))
                    if (step.canChangeMethod) {
                        TextButton(onClick = actions::onBack, enabled = !login.isWorking) {
                            Text("Другой способ")
                        }
                    }
                }
            }
            Button(
                text = "Продолжить",
                onClick = submit,
                state = if (login.isWorking) ButtonState.Loading else ButtonState.Default,
                modifier = Modifier
                    .adaptiveContentWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ResendButton(resend: Resend.AvailableAt, isWorking: Boolean, onClick: () -> Unit) {
    var secondsLeft by remember(resend.at) { mutableStateOf(resend.secondsLeft()) }
    LaunchedEffect(resend.at) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft = resend.secondsLeft()
        }
    }
    TextButton(onClick = onClick, enabled = secondsLeft == 0L && !isWorking) {
        Text(
            if (secondsLeft > 0) {
                "Отправить повторно через %d:%02d".format(secondsLeft / 60, secondsLeft % 60)
            } else {
                "Отправить код повторно"
            }
        )
    }
}

@OptIn(ExperimentalTime::class)
private fun Resend.AvailableAt.secondsLeft(): Long =
    (at - Clock.System.now()).inWholeSeconds.coerceAtLeast(0)

private fun VerificationMethod.destinationText(): String =
    when (kind) {
        VerificationKind.Totp -> "Код из приложения-аутентификатора"
        VerificationKind.TemporaryCode -> "Временный код, выданный университетом"
        else -> "Код отправлен в $label"
    }

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun CodePreview() {
    MpeixTheme {
        val step = LoginStep.EnterCode(
            method = VerificationMethod(VerificationMethodId("1"), VerificationKind.Telegram, "Telegram"),
            resend = Resend.AvailableAt(Instant.DISTANT_PAST),
            canChangeMethod = true,
        )
        CodeLayout(
            step = step,
            login = previewLoginState(step, error = LoginError.InvalidCode),
            actions = NoOpActions,
        )
    }
}
