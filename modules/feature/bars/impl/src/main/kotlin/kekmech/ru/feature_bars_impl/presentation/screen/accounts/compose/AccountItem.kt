package kekmech.ru.feature_bars_impl.presentation.screen.accounts.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import io.github.tonykolomeytsev.mpeix.sdk.Account
import io.github.tonykolomeytsev.mpeix.sdk.AccountId
import io.github.tonykolomeytsev.mpeix.sdk.AccountStatus
import io.github.tonykolomeytsev.mpeix.sdk.Subaccount
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun AccountItem(
    account: Account,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    editMode: Boolean = false,
    onClickDelete: () -> Unit = { /* no-op */ },
) {
    val name = account.displayName ?: account.username
    val bottomText = remember(account.subaccounts) {
        account.subaccounts
            .mapNotNull { it.group }
            .takeIf { it.isNotEmpty() }
            ?.joinToString(", ")
    }
    ListItem(
        headlineText = name,
        supportingText = bottomText,
        modifier = modifier
            .then(if (editMode) Modifier else Modifier.clickable(onClick = onClick)),
        trailingContent = {
            if (editMode) {
                IconButton(
                    onClick = onClickDelete,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                    )
                }
            } else when (account.status) {
                AccountStatus.Unfinished -> Text(stringResource(Strings.bars_accounts_status_unfinished))
                AccountStatus.SignedIn -> {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }

                AccountStatus.SessionExpired,
                AccountStatus.SignedOut -> Text(stringResource(Strings.bars_accounts_status_login_required))
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun AccountItemPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val accounts = listOf(
                Account(
                    id = AccountId("1"),
                    username = "PupkinVI",
                    displayName = "Пупкин Василий Иванович",
                    status = AccountStatus.SignedIn,
                    subaccounts = listOf(
                        Subaccount(
                            id = SubaccountId("123"),
                            title = "Пупкин Василий Иванович",
                            group = "C-12-16",
                            educationLevel = null,
                        ),
                    ),
                    lastSignedInAt = null,
                ),
                Account(
                    id = AccountId("1"),
                    username = "FedorovVI",
                    displayName = "Федоров Евгений Васильевич",
                    status = AccountStatus.SessionExpired,
                    subaccounts = listOf(
                        Subaccount(
                            id = SubaccountId("123"),
                            title = "Федоров Евгений Васильевич",
                            group = "А-08-16",
                            educationLevel = null,
                        ),
                    ),
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
            )
            accounts.fastForEach { account ->
                AccountItem(
                    account = account,
                    onClick = { /* no-op */ }
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun EditAccountItemPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val accounts = listOf(
                Account(
                    id = AccountId("1"),
                    username = "PupkinVI",
                    displayName = "Пупкин Василий Иванович",
                    status = AccountStatus.SignedIn,
                    subaccounts = listOf(
                        Subaccount(
                            id = SubaccountId("123"),
                            title = "Пупкин Василий Иванович",
                            group = "C-12-16",
                            educationLevel = null,
                        ),
                    ),
                    lastSignedInAt = null,
                ),
                Account(
                    id = AccountId("1"),
                    username = "FedorovVI",
                    displayName = "Федоров Евгений Васильевич",
                    status = AccountStatus.SessionExpired,
                    subaccounts = listOf(
                        Subaccount(
                            id = SubaccountId("123"),
                            title = "Федоров Евгений Васильевич",
                            group = "А-08-16",
                            educationLevel = null,
                        ),
                    ),
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
            )
            accounts.fastForEach { account ->
                AccountItem(
                    account = account,
                    onClick = { /* no-op */ },
                    editMode = true,
                )
            }
        }
    }
}