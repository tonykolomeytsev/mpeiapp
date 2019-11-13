package kekmech.ru.bars.main.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore

interface BarsFragmentModel {
    val isLoggedIn: Boolean

    var isNotShowedUpdateDialog: Boolean

    suspend fun getAcademicScoreAsync(refresh: Boolean = false, onRatingUpdatesListener: (AcademicScore?) -> Unit)

    suspend fun getAcademicScore(refresh: Boolean = false): AcademicScore?

    fun saveUserSecrets(login: String, pass: String)

    fun clearUserSecrets()

    fun setCurrentDiscipline(discipline: AcademicDiscipline)

    fun saveForceUpdateArgs(url: String, description: String)
}