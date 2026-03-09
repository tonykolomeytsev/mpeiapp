package kekmech.ru.feature_bars_impl.presentation.screen.login.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEffect
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginPasswordState
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.ui_kit_buttons.LegacyButton
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun BarsLoginPasswordScreen(
    onAccept: (BarsLoginEvent) -> Unit,
    state: BarsLoginPasswordState,
    effects: Flow<BarsLoginEffect>,
) {
    var login by rememberSaveable { mutableStateOf(state.login) }
    var password by rememberSaveable { mutableStateOf(state.password) }
    var wrongCredentialsAlert by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Вход в БАРС МЭИ",
                navigationIcon = { BackIconButton { onAccept(Ui.Click.Back) } },
            )
        },
        bottomBar = {
            val isContinueEnabled by remember {
                derivedStateOf { login.isNotEmpty() && password.isNotEmpty() }
            }
            LegacyButton(
                text = "Продолжить",
                onClick = { onAccept(Ui.Click.SubmitLoginPassword(login, password)) },
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
                value = login,
                onValueChange = {
                    wrongCredentialsAlert = false
                    login = it
                },
                modifier = Modifier.fillMaxWidth(),
                shape = shape,
                label = { Text("Логин") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    wrongCredentialsAlert = false
                    password = it
                },
                modifier = Modifier.fillMaxWidth(),
                shape = shape,
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Bold,
                ),
                supportingText = {
                    if (wrongCredentialsAlert) {
                        Text(
                            text = "Неверный логин или пароль",
                            color = MpeixTheme.palette.redMarkColor,
                        )
                    }
                }
            )
            Text(
                text = buildAnnotatedString {
                    append("Нажимая «Продолжить» вы соглашаетесь с ")
                    withLink(
                        LinkAnnotation.Url(
                            url = "https://mpeixprivacypolicy.blogspot.com/p/privacy-policy-kekmech-robotics-built.html",
                            styles = TextLinkStyles(style = SpanStyle(color = MpeixTheme.palette.primary)),
                        )
                    ) {
                        append("политикой конфиденциальности")
                    }
                },
                style = MpeixTheme.typography.paragraphNormal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
            if (state.failure != null) {
                ErrorBlock(state.failure)
            }
        }
    }

    EffectHandler(effects) { effect ->
        when (effect) {
            is BarsLoginEffect.ShowInvalidCredsText -> wrongCredentialsAlert = true
            else -> Unit
        }
    }
}

@Preview
@Composable
private fun BarsLoginPasswordScreenPreview() {
    MpeixTheme {
        BarsLoginPasswordScreen(
            onAccept = { /* no-op */ },
            state = BarsLoginPasswordState(
                failure = ExceptionWithId(RuntimeException()),
            ),
            effects = emptyFlow(),
        )
    }
}