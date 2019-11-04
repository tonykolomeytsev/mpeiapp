package kekmech.ru.bars.main.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicScore

interface BarsFragmentModel {
    val isLoggedIn: Boolean

    suspend fun getAcademicScoreAsync(onRatingUpdatesListener: (AcademicScore?) -> Unit)

    suspend fun getAcademicScore(): AcademicScore?

    fun saveUserSecrets(login: String, pass: String)

    fun clearUserSecrets()
}