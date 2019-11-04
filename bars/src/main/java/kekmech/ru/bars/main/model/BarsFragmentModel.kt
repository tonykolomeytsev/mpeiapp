package kekmech.ru.bars.main.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicScore

interface BarsFragmentModel {
    val isLoggedIn: Boolean

    suspend fun getAcademicScoreAsync(refresh: Boolean = false, onRatingUpdatesListener: (AcademicScore?) -> Unit)

    suspend fun getAcademicScore(refresh: Boolean = false): AcademicScore?

    fun saveUserSecrets(login: String, pass: String)

    fun clearUserSecrets()
}