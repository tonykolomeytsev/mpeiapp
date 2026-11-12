package kekmech.ru.feature_bars_impl.presentation.screen.accounts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import io.github.tonykolomeytsev.mpeix.sdk.Account
import io.github.tonykolomeytsev.mpeix.sdk.AccountId
import io.github.tonykolomeytsev.mpeix.sdk.AccountStatus
import io.github.tonykolomeytsev.mpeix.sdk.Subaccount
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import kekmech.ru.feature_bars_impl.presentation.screen.accounts.compose.AccountItem
import kekmech.ru.feature_bars_impl.presentation.shortName
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_kit_dialogs.AlertDialog
import kekmech.ru.ui_kit_dialogs.rememberAlertDialogState
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_theme.theme.MpeixTheme

/** Known accounts and "Add account". */
@Composable
internal fun BarsAccountsScreen(
    state: BarsAccountsState,
    actions: BarsAccountsActions,
) {
    DeleteAccountDialog(
        account = state.accountToDelete,
        onConfirm = actions::onConfirmDeleteAccount,
        onDismiss = actions::onDismissDeleteAccount,
    )
    Column {
        Text(
            text = stringResource(Strings.bars_accounts_title),
            style = MpeixTheme.typography.titleLarge,
            color = MpeixTheme.palette.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        val hasAccounts = !state.accounts.isNullOrEmpty()
        if (!hasAccounts) {
            Text(
                text = stringResource(Strings.bars_accounts_empty),
                style = MpeixTheme.typography.bodyLarge,
                color = MpeixTheme.palette.onSurfaceVariant,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp)),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            if (hasAccounts) {
                AccountsBlock(
                    accounts = state.accounts,
                    editMode = state.editMode,
                    onClick = actions::onAccountClick,
                    onClickDelete = actions::onDeleteAccountClick,
                )
            }
            // The buttons stay in composition and animate in and out, so the sheet changes its height smoothly
            AnimatedVisibility(visible = !state.editMode) {
                AddButtonBlock(
                    onClick = actions::onAddAccount,
                )
            }
            AnimatedVisibility(visible = hasAccounts) {
                AnimatedContent(
                    targetState = state.editMode,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "EditAccountsToggle",
                ) { editMode ->
                    if (editMode) {
                        FinishEditingAccountsBlock(
                            onClick = actions::onFinishEditing,
                        )
                    } else {
                        EditAccountsBlock(
                            onClick = actions::onEditAccounts,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditAccountsBlock(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineText = stringResource(Strings.bars_accounts_edit),
        leadingContent = {
            Icon(Icons.Outlined.Settings)
        },
        modifier = modifier
            .background(MpeixTheme.palette.surfaceContainer)
            .clickable(onClick = onClick),
    )
}

@Composable
private fun FinishEditingAccountsBlock(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineText = stringResource(Strings.bars_accounts_finish_editing),
        leadingContent = {
            Icon(Icons.Outlined.Done)
        },
        modifier = modifier
            .background(MpeixTheme.palette.surfaceContainer)
            .clickable(onClick = onClick),
    )
}

@Composable
private fun AddButtonBlock(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineText = stringResource(Strings.bars_accounts_add),
        leadingContent = {
            Icon(Icons.Default.Add)
        },
        modifier = modifier
            .background(MpeixTheme.palette.surfaceContainer)
            .clickable(onClick = onClick),
    )
}

@Composable
private fun AccountsBlock(
    accounts: List<Account>,
    onClick: (Account) -> Unit,
    modifier: Modifier = Modifier,
    editMode: Boolean = false,
    onClickDelete: (Account) -> Unit = { /* no-op */ },
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.surfaceContainer),
    ) {
        accounts.fastForEach { account ->
            key(account.id) {
                AccountItem(
                    account = account,
                    onClick = { onClick.invoke(account) },
                    editMode = editMode,
                    onClickDelete = { onClickDelete.invoke(account) },
                )
            }
        }
    }
}

/** Asks before [BarsAccountsActions.onConfirmDeleteAccount]: removing the account wipes all its cached data. */
@Composable
private fun DeleteAccountDialog(
    account: Account?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogState = rememberAlertDialogState(dialogVisible = account != null)
    // Keeps the name in the text while the dialog fades out
    var lastAccount by remember { mutableStateOf(account) }
    LaunchedEffect(account) {
        if (account != null) {
            lastAccount = account
            dialogState.showDialog()
        } else {
            dialogState.hideDialog()
        }
    }
    val name = lastAccount?.shortName.orEmpty()
    AlertDialog(
        onDismissRequest = onDismiss,
        state = dialogState,
        title = stringResource(Strings.bars_accounts_delete_title),
        text = stringResource(Strings.bars_accounts_delete_text, name),
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(Strings.bars_accounts_delete_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Strings.bars_accounts_delete_cancel))
            }
        },
    )
}

private object NoOpAccountsActions : BarsAccountsActions {
    override fun onBack() = Unit
    override fun onAddAccount() = Unit
    override fun onAccountClick(account: Account) = Unit
    override fun onEditAccounts() = Unit
    override fun onFinishEditing() = Unit
    override fun onDeleteAccountClick(account: Account) = Unit
    override fun onConfirmDeleteAccount() = Unit
    override fun onDismissDeleteAccount() = Unit
}

@Preview
@Composable
private fun NoAccountsPreview() {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            BarsAccountsScreen(
                state = BarsAccountsState(),
                actions = NoOpAccountsActions,
            )
        }
    }
}

@Preview
@Composable
private fun AccountsPreview() {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            BarsAccountsScreen(
                state = BarsAccountsState(
                    accounts = listOf(
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
                    ),
                    editMode = false,
                ),
                actions = NoOpAccountsActions,
            )
        }
    }
}

@Composable
@Preview
private fun EditPreview() {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            BarsAccountsScreen(
                state = BarsAccountsState(
                    accounts = listOf(
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
                    ),
                    editMode = true,
                ),
                actions = NoOpAccountsActions,
            )
        }
    }
}
