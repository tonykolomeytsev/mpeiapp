package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
internal fun VerificationMethodsLayout(
    step: LoginStep.ChooseVerificationMethod,
    login: LoginState,
    actions: BarsLoginActions,
) {
    SheetLayout(title = "Способ подтверждения", onBack = actions::onBack, isWorking = login.isWorking) {
        Description("Куда отправить код для входа?")
        step.methods.forEach { method ->
            ListItem(
                headlineText = method.label,
                supportingText = method.maskedDestination,
                leadingContent = { Icon(method.kind.icon()) },
                modifier = Modifier.clickable(enabled = !login.isWorking) {
                    actions.onChooseVerificationMethod(method.id)
                },
            )
        }
        login.error?.let { ErrorText(it, Modifier.padding(horizontal = 16.dp)) }
    }
}

private fun VerificationKind.icon(): ImageVector =
    when (this) {
        VerificationKind.Email -> Icons.Outlined.Email
        VerificationKind.Sms -> Icons.Outlined.Phone
        VerificationKind.Totp,
        VerificationKind.OneTimeCode -> Icons.Outlined.Lock
        else -> Icons.Outlined.Send
    }

@Preview
@Composable
private fun VerificationMethodsPreview() = LayoutPreview {
    val step = LoginStep.ChooseVerificationMethod(
        listOf(
            VerificationMethod(VerificationMethodId("tg"), VerificationKind.Telegram, "Telegram", "@iva***"),
            VerificationMethod(VerificationMethodId("max"), VerificationKind.Max, "MAX", "+7 *** ***-12-34"),
            VerificationMethod(VerificationMethodId("totp"), VerificationKind.Totp, "Приложение", null),
        )
    )
    VerificationMethodsLayout(
        step = step,
        login = previewLoginState(step, isWorking = true),
        actions = NoOpActions,
    )
}
