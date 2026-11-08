package kekmech.ru.feature_bars_impl.presentation.screen.accounts

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.tonykolomeytsev.mpeix.sdk.Account
import io.github.tonykolomeytsev.mpeix.sdk.AccountStatus
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.ProfileId
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.navigation.BarsLoginScreenKey
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

internal interface BarsAccountsActions {
    fun onBack()
    fun onAddAccount()
    fun onAccountClick(account: Account)
    fun onEditAccounts()
    fun onFinishEditing()
    fun onDeleteAccountClick(account: Account)
    fun onConfirmDeleteAccount()
    fun onDismissDeleteAccount()
}

@Immutable
internal data class BarsAccountsState(
    /** Null until the first emission of the SDK. */
    val accounts: List<Account>? = null,
    val editMode: Boolean = false,
    /** Account waiting for the confirmation of the deletion. */
    val accountToDelete: Account? = null,
)

/**
 * Known accounts. A signed in account becomes the current profile, others and "Add account" open the login screen.
 * In the edit mode accounts can be removed together with their cached data.
 */
internal class BarsAccountsViewModel(
    private val sdk: MpeixSdk,
    private val router: Router,
    private val barsProfileIdRepository: BarsProfileIdRepository,
) : ViewModel(), BarsAccountsActions {

    val state: StateFlow<BarsAccountsState>
        field = MutableStateFlow(BarsAccountsState())

    init {
        sdk.bars.auth.accounts
            .onEach { accounts ->
                state.update {
                    it.copy(
                        accounts = accounts,
                        // Nothing left to edit
                        editMode = it.editMode && accounts.isNotEmpty(),
                        accountToDelete = it.accountToDelete?.takeIf { pending ->
                            accounts.any { account -> account.id == pending.id }
                        },
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onBack() {
        router.executeCommand(PopBackStack())
    }

    override fun onAddAccount() {
        router.executeCommand(AddScreenForward(BarsLoginScreenKey()))
    }

    override fun onAccountClick(account: Account) {
        val profileId = account.profileIdToSwitch()
        if (profileId != null) {
            barsProfileIdRepository.saveCurrentProfileId(profileId)
            router.executeCommand(PopBackStack())
        } else {
            // Expired session, unfinished login: the SDK prefills the username or resumes the live attempt
            router.executeCommand(AddScreenForward(BarsLoginScreenKey(account.id.value)))
        }
    }

    override fun onEditAccounts() {
        state.update { it.copy(editMode = !it.accounts.isNullOrEmpty()) }
    }

    override fun onFinishEditing() {
        state.update { it.copy(editMode = false, accountToDelete = null) }
    }

    override fun onDeleteAccountClick(account: Account) {
        state.update { it.copy(accountToDelete = account) }
    }

    override fun onDismissDeleteAccount() {
        state.update { it.copy(accountToDelete = null) }
    }

    override fun onConfirmDeleteAccount() {
        val account = state.value.accountToDelete ?: return
        state.update { it.copy(accountToDelete = null) }
        // The main screen must not keep showing the profile whose cache is being wiped
        if (barsProfileIdRepository.currentProfileId.value?.accountId == account.id) {
            barsProfileIdRepository.clearCurrentProfileId()
        }
        viewModelScope.launch {
            try {
                sdk.bars.auth.removeAccount(account.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Timber.e(e, "Failed to remove BARS account")
            }
        }
    }

    /**
     * Null if the account needs the login first. Keeps the current subaccount if this account is already active,
     * otherwise takes the last one.
     */
    private fun Account.profileIdToSwitch(): ProfileId? {
        if (status != AccountStatus.SignedIn) return null
        val current = barsProfileIdRepository.currentProfileId.value
        val subaccount = subaccounts.firstOrNull { current?.accountId == id && it.id == current.subaccountId }
            ?: subaccounts.lastOrNull()
            ?: return null
        return ProfileId(id, subaccount.id)
    }
}
