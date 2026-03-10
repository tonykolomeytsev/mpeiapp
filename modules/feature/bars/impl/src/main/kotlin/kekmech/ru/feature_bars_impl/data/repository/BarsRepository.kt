package kekmech.ru.feature_bars_impl.data.repository

import android.os.Parcelable
import com.kekmech.lib_bars.BarsHandle
import com.tencent.mmkv.MMKV
import kekmech.ru.ext_kotlin.fromBase64
import kekmech.ru.ext_kotlin.toBase64
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.Account
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.parcelize.Parcelize

internal class BarsRepository(
    private val barsHandle: BarsHandle,
) : BarsLogoutHandle {

    private val storage = MMKV.mmkvWithID("bars")

    val loginStateTrigger = MutableSharedFlow<Unit>(replay = 1)

    init {
        loginStateTrigger.tryEmit(Unit)
    }

    fun saveCurrentUserId(id: String) {
        storage.encode("current_user_id", id)
    }

    fun saveAccounts(accounts: List<Account>) {
        storage.encode("all_user_ids", accounts.map { it.id }.toSet())
        for (acc in accounts) {
            val encodedContent = buildString {
                append(acc.id.toBase64())
                append(":")
                append(acc.name.toBase64())
                append(":")
                append(acc.group.toBase64())
                append(":")
                append(acc.status.toBase64())
            }
            storage.encode("user-${acc.id}", encodedContent)
        }
    }

    fun getCurrentAccount(): Account? {
        val currentId = storage.decodeString("current_user_id") ?: return null
        val encodedContent = storage.decodeString("user-${currentId}") ?: return null
        val parts = encodedContent.split(":")
        return Account(
            id = currentId,
            name = parts[1].fromBase64(),
            group = parts[2].fromBase64(),
            status = parts[3].fromBase64(),
        )
    }

    fun saveDisciplines(list: List<AssessedDiscipline>) {
        val currentId = storage.decodeString("current_user_id") ?: return
        val container = Container(list)
        storage.encode("user-$currentId-disciplines", container)
    }

    fun getSavedDisciplines(): List<AssessedDiscipline>? {
        val currentId = storage.decodeString("current_user_id") ?: return null
        val container =
            storage.decodeParcelable("user-$currentId-disciplines", Container::class.java)
        return container?.disciplines
    }

    override fun logout(): Boolean {
        storage.decodeString("current_user_id") ?: return false
        barsHandle.auth.logout()
        storage.removeValueForKey("current_user_id")
        loginStateTrigger.tryEmit(Unit)
        return true
    }

    @Parcelize
    private data class Container(val disciplines: List<AssessedDiscipline>) : Parcelable
}