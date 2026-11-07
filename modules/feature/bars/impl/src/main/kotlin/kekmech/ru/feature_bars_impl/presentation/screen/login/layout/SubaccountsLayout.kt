package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.Subaccount
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_kit_lists.ListItem

@Composable
internal fun SubaccountsLayout(step: LoginStep.ChooseSubaccount, login: LoginState, actions: BarsLoginActions) {
    SheetLayout(title = "Выберите профиль", isWorking = login.isWorking) {
        Description("У вашего аккаунта несколько ступеней обучения. Профиль можно будет сменить позже")
        step.subaccounts.forEach { subaccount ->
            ListItem(
                headlineText = subaccount.title,
                supportingText = listOfNotNull(subaccount.educationLevel, subaccount.group)
                    .joinToString(", ")
                    .takeIf { it.isNotEmpty() && !it.equals(subaccount.title, ignoreCase = true) },
                modifier = Modifier.clickable(enabled = !login.isWorking) {
                    actions.onChooseSubaccount(subaccount.id)
                },
            )
        }
        login.error?.let { ErrorText(it, Modifier.padding(horizontal = 16.dp)) }
    }
}

@Preview
@Composable
private fun SubaccountsPreview() = LayoutPreview {
    val step = LoginStep.ChooseSubaccount(
        listOf(
            Subaccount(SubaccountId("1"), "Бакалавриат, А-08-19", "А-08-19", "Бакалавриат"),
            Subaccount(SubaccountId("2"), "Магистратура, А-08м-23", "А-08м-23", "Магистратура"),
        )
    )
    SubaccountsLayout(step = step, login = previewLoginState(step), actions = NoOpActions)
}
