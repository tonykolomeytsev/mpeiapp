package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val PRIVACY_POLICY_URL =
    "https://mpeixprivacypolicy.blogspot.com/p/privacy-policy-kekmech-robotics-built.html"

@Composable
internal fun PasswordLayout(step: LoginStep.EnterPassword, login: LoginState, actions: BarsLoginActions) {
    var password by rememberSaveable { mutableStateOf("") }
    val submit = { if (password.isNotEmpty()) actions.onSubmitPassword(password) }
    SheetLayout(title = "Введите пароль", subtitle = step.username, onBack = actions::onBack) {
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            singleLine = true,
            enabled = !login.isWorking,
            isError = login.error != null,
            supportingText = login.error?.let { { ErrorText(it) } },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = LocalTextStyle.current.copy(letterSpacing = 6.sp, fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() }),
            shape = FieldShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(rememberAutoFocus()),
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
        PrimaryButton(text = "Продолжить", isWorking = login.isWorking, onClick = submit)
    }
}

@Preview
@Composable
private fun PasswordPreview() = LayoutPreview {
    val step = LoginStep.EnterPassword("IvanovII")
    PasswordLayout(
        step = step,
        login = previewLoginState(step, error = LoginError.InvalidCredentials),
        actions = NoOpActions,
    )
}
