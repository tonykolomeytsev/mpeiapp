package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions

@Composable
internal fun UsernameLayout(step: LoginStep.EnterUsername, login: LoginState, actions: BarsLoginActions) {
    var username by rememberSaveable { mutableStateOf(step.prefilled.orEmpty()) }
    val submit = { if (username.isNotBlank()) actions.onSubmitUsername(username) }
    SheetLayout(title = "Введите логин", onBack = actions::onBack) {
        Description("Логин от БАРС, например IvanovII")
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") },
            singleLine = true,
            enabled = !login.isWorking,
            isError = login.error != null,
            supportingText = login.error?.let { { ErrorText(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { submit() }),
            shape = FieldShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(rememberAutoFocus()),
        )
        PrimaryButton(text = "Продолжить", isWorking = login.isWorking, onClick = submit)
    }
}
