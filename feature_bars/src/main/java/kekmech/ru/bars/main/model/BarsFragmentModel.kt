package kekmech.ru.bars.main.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore

interface BarsFragmentModel {
    val isLoggedIn: LiveData<Boolean>

    val score: LiveData<AcademicScore>

    var isNotShowedUpdateDialog: Boolean

    var ratingDetails: AcademicScore.Rating?

    var realUpdateIndex: Int

    var lastUpdateIndex: Int

    val hasUserCredentials: Boolean

    suspend fun getAcademicScoreAsync(refresh: Boolean = false, onRatingUpdatesListener: (AcademicScore?) -> Unit)

    suspend fun getAcademicScore(refresh: Boolean = false): AcademicScore?

    fun saveUserSecrets(login: String, pass: String)

    fun logout()

    fun setCurrentDiscipline(discipline: AcademicDiscipline)

    fun saveForceUpdateArgs(url: String, description: String)

    suspend fun updateScore()

    fun getLoginScript(): String
    fun setUserAgent(ua: String)
}