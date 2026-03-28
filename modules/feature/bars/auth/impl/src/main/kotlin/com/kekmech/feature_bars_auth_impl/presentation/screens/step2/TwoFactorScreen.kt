package com.kekmech.feature_bars_auth_impl.presentation.screens.step2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kekmech.feature_bars_auth_impl.domain.Auth2faProvider
import com.kekmech.feature_bars_auth_impl.presentation.components.Banner
import com.kekmech.feature_bars_auth_impl.presentation.components.ResendCodeBlock
import com.kekmech.feature_bars_auth_impl.presentation.components.rememberBannerState
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.CodeState
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent.Ui
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorStoreFactory
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.ui_preview.MpeixPreview
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_kit_buttons.LegacyButton
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorState as State

@Composable
internal fun TwoFactorScreen() {
    val factory = koinInject<TwoFactorStoreFactory>()
    ElmContent(
        key = "TwoFactorScreen",
        storeFactory = { factory.create() },
        content = ::TwoFactorScreen,
    )
}

@Composable
private fun TwoFactorScreen(
    onAccept: (Event) -> Unit,
    state: State,
    effects: Flow<Effect>,
) {
    var code by rememberSaveable { mutableStateOf("") }
    var wrongCodeAlert by remember { mutableStateOf(false) }
    val router = LocalRouter.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Вход в БАРС МЭИ",
                navigationIcon = {
                    BackIconButton {
                        router.executeCommand(PopBackStack())
                    }
                },
            )
        },
        bottomBar = {
            val isContinueEnabled by remember {
                derivedStateOf { code.isNotEmpty() }
            }
            LegacyButton(
                text = "Продолжить",
                onClick = { onAccept(Ui.Action.SubmitCode(code)) },
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
                debounceSec = state.debounceSec,
                onClickResend = { onAccept(Ui.Action.ResendCode(it)) },
            )
        }
    }

    val bannerState = rememberBannerState()
    Banner(
        state = bannerState,
        modifier = Modifier.statusBarsPadding(),
    )

    val currentScope = rememberCoroutineScope()
    EffectHandler(effects) { effect ->
        when (effect) {
            is Effect.ShowInvalidCodeAlert -> wrongCodeAlert = true
            is Effect.ShowNetworkErrorBanner -> {
                currentScope.launch {
                    bannerState.show("Проблема с сетью. Попробуйте еще раз")
                }
            }

            is Effect.ShowFatalErrorBanner -> {
                currentScope.launch {
                    bannerState.show("Критическая ошибка")
                }
            }

            is Effect.ShowServerErrorBanner -> {
                currentScope.launch {
                    bannerState.show("Сообщение от сайта МЭИ:\n${effect.message}")
                }
            }
        }
    }
}

@Preview
@Composable
private fun TwoFactorScreenPreview() {
    MpeixPreview {
        TwoFactorScreen(
            onAccept = { /* no-op */ },
            state = State(
                codeState = CodeState.CodeSent(
                    provider = Auth2faProvider("1", "Telegram", false),
                ),
                providers = listOf(
                    Auth2faProvider("1", "Telegram", false),
                    Auth2faProvider("2", "Вконтакте", false),
                ),
            ),
            effects = emptyFlow(),
        )
    }
}
