package kekmech.ru.feature_bars_impl.presentation.screen.login.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEffect
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsTwoFactorCodeState
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeProvider
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeState
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.ui_kit_buttons.LegacyButton
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun BarsTwoFactorCodeScreen(
    onAccept: (BarsLoginEvent) -> Unit,
    state: BarsTwoFactorCodeState,
    effects: Flow<BarsLoginEffect>,
) {
    var code by rememberSaveable { mutableStateOf("") }
    var wrongCodeAlert by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Вход в БАРС МЭИ",
                navigationIcon = {
                    BackIconButton { onAccept(Ui.Click.Back) }
                },
            )
        },
        bottomBar = {
            val isContinueEnabled by remember {
                derivedStateOf { code.isNotEmpty() }
            }
            LegacyButton(
                text = "Продолжить",
                onClick = { onAccept(Ui.Click.SubmitCode(code)) },
                enabled = isContinueEnabled,
                loading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        containerColor = MpeixTheme.palette.background,
        contentColor = MpeixTheme.palette.content,
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            val shape = RoundedCornerShape(8.dp)
            OutlinedTextField(
                value = code,
                onValueChange = {
                    wrongCodeAlert = false
                    code = it
                },
                modifier = Modifier.fillMaxWidth(),
                shape = shape,
                label = { Text("Код из мессенджера") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                supportingText = {
                    if (wrongCodeAlert) {
                        Text(
                            text = "Введен неверный код",
                            color = MpeixTheme.palette.redMarkColor,
                        )
                    }
                }
            )
            ResendCodeBlock(
                providers = state.providers,
                codeState = state.codeState,
                onClickResend = { onAccept(Ui.Click.Resend(it)) },
            )
            if (state.failure != null) {
                ErrorBlock(state.failure)
            }
        }
    }

    EffectHandler(effects) { effect ->
        when (effect) {
            is BarsLoginEffect.ShowInvalidCodeText -> wrongCodeAlert = true
            else -> Unit
        }
    }
}

@Preview
@Composable
private fun BarsTwoFactorCodeScreenPreview() {
    MpeixTheme {
        BarsTwoFactorCodeScreen(
            onAccept = { /* no-op */ },
            state = BarsTwoFactorCodeState(
                codeState = CodeState.SendingCode(CodeProvider.TG),
            ),
            effects = emptyFlow(),
        )
    }
}

@Preview
@Composable
private fun BarsTwoFactorCodeScreenPreview2() {
    MpeixTheme {
        BarsTwoFactorCodeScreen(
            onAccept = { /* no-op */ },
            state = BarsTwoFactorCodeState(
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.TG,
                    resendDebounceSec = 15,
                ),
                providers = listOf(CodeProvider.TG, CodeProvider.VK),
            ),
            effects = emptyFlow(),
        )
    }
}

@Preview
@Composable
private fun BarsTwoFactorCodeScreenPreview3() {
    MpeixTheme {
        BarsTwoFactorCodeScreen(
            onAccept = { /* no-op */ },
            state = BarsTwoFactorCodeState(
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.TG,
                    resendDebounceSec = 0,
                ),
                providers = listOf(CodeProvider.TG, CodeProvider.VK),
                failure = ExceptionWithId(RuntimeException()),
            ),
            effects = flowOf(BarsLoginEffect.ShowInvalidCodeText),
        )
    }
}