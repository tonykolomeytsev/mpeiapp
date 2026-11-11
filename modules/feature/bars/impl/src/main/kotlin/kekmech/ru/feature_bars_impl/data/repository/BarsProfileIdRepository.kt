package kekmech.ru.feature_bars_impl.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.ProfileId
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Keeps the [ProfileId] of the current user. The site session lives inside the SDK,
 * so the profile stays the same after the session expires and its cached data stays reachable.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class BarsProfileIdRepository(
    private val sdk: MpeixSdk,
    private val preferences: SharedPreferences,
) : BarsLogoutHandle {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val currentProfileId: StateFlow<ProfileId?>
        field: MutableStateFlow<ProfileId?> = MutableStateFlow(
            preferences.getString(CURRENT_PROFILE_ID_KEY, null)?.let(ProfileId::decode)
        )

    init {
        // The saved id may point to an account the SDK does not know: written by another runtime (stub builds keep
        // their own storage), or the SDK files were wiped. Every read of it would wait forever, so it is dropped
        scope.launch {
            currentProfileId
                .filterNotNull()
                .flatMapLatest { id -> sdk.bars.auth.account(id.accountId).map { account -> id to account } }
                .collect { (id, account) ->
                    if (account == null && currentProfileId.value == id) clearCurrentProfileId()
                }
        }
    }

    fun saveCurrentProfileId(id: ProfileId) {
        preferences.edit { putString(CURRENT_PROFILE_ID_KEY, id.encode()) }
        currentProfileId.value = id
    }

    /** Forgets the current profile without touching the SDK session. */
    fun clearCurrentProfileId() {
        preferences.edit { remove(CURRENT_PROFILE_ID_KEY) }
        currentProfileId.value = null
    }

    override fun logout(): Boolean {
        val profileId = currentProfileId.value ?: return false
        clearCurrentProfileId()
        scope.launch { sdk.bars.auth.signOut(profileId.accountId) }
        return true
    }

    private companion object {
        const val CURRENT_PROFILE_ID_KEY = "bars_current_profile_id"
    }
}
