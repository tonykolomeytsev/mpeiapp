package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonState
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val PRIVACY_POLICY_URL =
    "https://mpeixprivacypolicy.blogspot.com/p/privacy-policy-kekmech-robotics-built.html"

@Composable
internal fun CredentialsLayout(
    step: LoginStep.EnterCredentials,
    login: LoginState,
    actions: BarsLoginActions,
) {
    var username by rememberSaveable { mutableStateOf(step.prefilledUsername.orEmpty()) }
    var password by rememberSaveable { mutableStateOf("") }
    val submit = {
        if (username.isNotBlank() && password.isNotEmpty()) actions.onSubmitCredentials(
            username,
            password
        )
    }
    // With the prefilled username (resumed account) the user only has to type the password
    val autoFocus = rememberAutoFocus()
    val otherFocus = remember { FocusRequester() }
    val isPrefilled = !step.prefilledUsername.isNullOrEmpty()
    val usernameFocus = if (isPrefilled) otherFocus else autoFocus
    val passwordFocus = if (isPrefilled) autoFocus else otherFocus
    Scaffold(
        // Back closes the screen on the first step, so there is no cross
        topBar = { TopAppBar(navigationIcon = { BackIconButton(actions::onBack) }) },
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
                    text = "Вход в БАРС",
                    style = MpeixTheme.typography.headlineBig,
                    color = MpeixTheme.palette.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Text(
                    text = "Логин и пароль от БАРС, например IvanovII",
                    style = MpeixTheme.typography.bodyLarge,
                    color = MpeixTheme.palette.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Логин") },
                    singleLine = true,
                    enabled = !login.isWorking,
                    isError = login.error is LoginError.InvalidCredentials,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .focusRequester(usernameFocus),
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
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
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = LocalTextStyle.current.copy(
                        letterSpacing = 6.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .focusRequester(passwordFocus),
                )
                Text(
                    text = buildAnnotatedString {
                        append("Нажимая «Продолжить», вы соглашаетесь с ")
                        withLink(
                            LinkAnnotation.Url(
                                url = PRIVACY_POLICY_URL,
                                styles = TextLinkStyles(style = SpanStyle(color = MpeixTheme.palette.primary)),
                            )
                        ) {
                            append("политикой конфиденциальности")
                        }
                    },
                    style = MpeixTheme.typography.bodyMedium,
                    color = MpeixTheme.palette.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
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

@Preview
@Composable
private fun CredentialsPreview() {
    val step = LoginStep.EnterCredentials(prefilledUsername = null)
    CredentialsLayout(step = step, login = previewLoginState(step), actions = NoOpActions)
}

@Preview
@Composable
private fun CredentialsErrorPreview() {
    val step = LoginStep.EnterCredentials(prefilledUsername = "IvanovII")
    CredentialsLayout(
        step = step,
        login = previewLoginState(step, error = LoginError.InvalidCredentials),
        actions = NoOpActions,
    )
}
