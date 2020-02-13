package kekmech.ru.core.repositories

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kotlinx.coroutines.Job

interface BarsRepository {

    var currentAcademicDiscipline: AcademicDiscipline?

    val hasUserCredentials: Boolean

    val isLoggedIn: LiveData<Boolean>

    val score: LiveData<AcademicScore>

    suspend fun updateScore()
    suspend fun getScoreAsync(forceRefresh: Boolean): AcademicScore?
    fun saveUserSecrets(username: String, password: String)
    fun clearUserSecrets()
    fun getLoginScript(): String


    companion object {
        const val BARS_URL = "https://bars.mpei.ru/bars_web/"
        const val BARS_LIST_OF_STUDENTS = "https://bars.mpei.ru/bars_web/Student/ListStudent"
    }
}