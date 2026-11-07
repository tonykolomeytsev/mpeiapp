package kekmech.ru.feature_bars_impl.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.ProfileId
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Keeps the [ProfileId] of the current user. The site session lives inside the SDK,
 * so the profile stays the same after the session expires and its cached data stays reachable.
 */
internal class BarsProfileIdRepository(
    private val sdk: MpeixSdk,
    private val preferences: SharedPreferences,
) : BarsLogoutHandle {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val currentProfileId: StateFlow<ProfileId?>
        field: MutableStateFlow<ProfileId?> = MutableStateFlow(
            preferences.getString(CURRENT_PROFILE_ID_KEY, null)?.let(ProfileId::decode)
        )

    fun saveCurrentProfileId(id: ProfileId) {
        preferences.edit { putString(CURRENT_PROFILE_ID_KEY, id.encode()) }
        currentProfileId.value = id
    }

    override fun logout(): Boolean {
        val profileId = currentProfileId.value ?: return false
        preferences.edit { remove(CURRENT_PROFILE_ID_KEY) }
        currentProfileId.value = null
        scope.launch { sdk.bars.auth.signOut(profileId.accountId) }
        return true
    }

    private companion object {
        const val CURRENT_PROFILE_ID_KEY = "bars_current_profile_id"
    }
}
