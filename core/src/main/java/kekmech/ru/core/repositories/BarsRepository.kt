package kekmech.ru.core.repositories

import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kotlinx.coroutines.Job

interface BarsRepository {

    var currentAcademicDiscipline: AcademicDiscipline?

    val isLoggedIn: Boolean

    val score: MutableLiveData<AcademicScore>

    suspend fun updateScore()

    suspend fun getScoreAsync(forceRefresh: Boolean): AcademicScore?

    fun saveUserSecrets(username: String, password: String)

    fun clearUserSecrets()


    companion object {
        const val BARS_URL = "https://bars.mpei.ru/bars_web/"
        const val BARS_LIST_OF_STUDENTS = "https://bars.mpei.ru/bars_web/Student/ListStudent"
    }
}