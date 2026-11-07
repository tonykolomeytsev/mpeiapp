package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.tonykolomeytsev.mpeix.sdk.Account
import io.github.tonykolomeytsev.mpeix.sdk.AccountId
import io.github.tonykolomeytsev.mpeix.sdk.AccountStatus
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_kit_buttons.button.ButtonPalette
import kekmech.ru.ui_kit_lists.ListItem

/** The first layout of the sheet: known accounts and "Add account". */
@Composable
internal fun AccountsLayout(accounts: List<Account>, actions: BarsLoginActions) {
    SheetLayout(title = "Вход в БАРС МЭИ") {
        if (accounts.isEmpty()) {
            Description(
                "Войдите в личный кабинет БАРС, чтобы смотреть оценки и баллы. " +
                    "Логин и пароль такие же, как на сайте bars.mpei.ru"
            )
        } else {
            accounts.forEach { account ->
                ListItem(
                    headlineText = account.displayName ?: account.username,
                    supportingText = account.statusText(),
                    leadingContent = {
                        Monogram((account.displayName ?: account.username).firstOrNull()?.uppercaseChar() ?: '?')
                    },
                    modifier = Modifier.clickable { actions.onAccountClick(account.id) },
                )
            }
        }
        PrimaryButton(
            text = if (accounts.isEmpty()) "Добавить аккаунт" else "Добавить новый аккаунт",
            palette = if (accounts.isEmpty()) ButtonPalette.Primary else ButtonPalette.Secondary,
            onClick = actions::onAddAccount,
        )
    }
}

private fun Account.statusText(): String =
    when (status) {
        AccountStatus.Unfinished -> "$username · вход не завершён"
        AccountStatus.SessionExpired -> "Сессия истекла"
        AccountStatus.SignedOut -> "Выполнен выход"
        AccountStatus.SignedIn -> subaccounts.joinToString { it.group ?: it.title }.ifEmpty { username }
        else -> username
    }

@Preview
@Composable
private fun NoAccountsPreview() = LayoutPreview {
    AccountsLayout(accounts = emptyList(), actions = NoOpActions)
}

@Preview
@Composable
private fun AccountsPreview() = LayoutPreview {
    AccountsLayout(
        accounts = listOf(
            Account(
                id = AccountId("1"),
                username = "IvanovII",
                displayName = "Иванов Иван Иванович",
                status = AccountStatus.SessionExpired,
                subaccounts = emptyList(),
                lastSignedInAt = null,
            ),
            Account(
                id = AccountId("2"),
                username = "PetrovPP",
                displayName = null,
                status = AccountStatus.Unfinished,
                subaccounts = emptyList(),
                lastSignedInAt = null,
            ),
        ),
        actions = NoOpActions,
    )
}
