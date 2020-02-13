package kekmech.ru.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.gateways.BarsHttpClient
import kekmech.ru.repository.gateways.BarsJsoupUtils
import kekmech.ru.repository.utils.BarsParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import org.jsoup.Jsoup

class BarsRepositoryImpl constructor(
    private val context: Context,
    private val baseKeyStore: BaseKeyStore
) : BarsRepository {
    private val client by lazy { BarsHttpClient(
        debug = false,
        hiddenFieldsExtractor = BarsJsoupUtils.hiddenFieldsExtractor(getUsername(), getPassword()),
        studentPageLocationExtractor = BarsJsoupUtils.studentPageLocationExtractor(),
        semesterListExtractor = BarsJsoupUtils.semesterListExtractor()
    ) }

    private val sharedPreferences = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)

    override val hasUserCredentials: Boolean
        get() = sharedPreferences.getString("user1", "")?.isNotEmpty() ?: false
    override val isLoggedIn = MutableLiveData<Boolean>().apply { value = false }
    override val score = MutableLiveData<AcademicScore>()

    override var currentAcademicDiscipline: AcademicDiscipline? = null
        set(value) {
            if (value != null) {
                field = value
                saveCurrentAcademicDiscipline(value)
            }
        }
        get() = if (field == null) loadCurrentAcademicDisciplineFromCache() else field

    private fun saveCurrentAcademicDiscipline(value: AcademicDiscipline) {
        val gson = GsonBuilder().create()
        sharedPreferences
            .edit()
            .putString("current_discipline", gson.toJson(value))
            .apply()
    }

    private fun loadCurrentAcademicDisciplineFromCache(): AcademicDiscipline? {
        val disciplineJson = sharedPreferences.getString("current_discipline", null)
        if (disciplineJson != null) {
            val gson = GsonBuilder().create()
            return try {
                val discipline = gson.fromJson(disciplineJson, AcademicDiscipline::class.java)
                discipline
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            return null
        }
    }


    override suspend fun getScoreAsync(forceRefresh: Boolean): AcademicScore? {
        return if (!forceRefresh)
            loadFromCache() ?: loadFromRemote()
        else
            loadFromRemote()
    }

    private fun loadFromCache(): AcademicScore? {
        val scoreJson = sharedPreferences.getString("score", null)
        if (scoreJson != null) {
            val gson = GsonBuilder().create()
            return try {
                val score = gson.fromJson(scoreJson, AcademicScore::class.java)
                score
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            return null
        }

    }

    private fun loadFromRemote(): AcademicScore? {
        try {
            if (client.studentId.isEmpty())
                client.loginWithWebkitCookies()

            GlobalScope.launch(Dispatchers.Main) { isLoggedIn.value = client.isLoggedIn }

            val score = BarsParser().parse(Jsoup.parse(client.loadSemesterPage()))
            saveToCache(score)
            return score

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun saveToCache(score: AcademicScore) {
        GlobalScope.launch(Dispatchers.Main) { this@BarsRepositoryImpl.score.value = score }
        val gson = GsonBuilder().create()
        sharedPreferences
            .edit()
            .putString("score", gson.toJson(score))
            .apply()
    }

    private fun getUsername(): String {
        val username = sharedPreferences
            .getString("user1", null) ?: throw RuntimeException("User is not logged in")
        return baseKeyStore.decrypt(username)
    }

    private fun getPassword(): String {
        val password = sharedPreferences
            .getString("user2", null) ?: throw RuntimeException("User is not logged in")
        return baseKeyStore.decrypt(password)
    }

    override fun saveUserSecrets(username: String, password: String) {
        sharedPreferences
            .edit()
            .putString("user1", baseKeyStore.encrypt(username))
            .putString("user2", baseKeyStore.encrypt(password))
            .apply()
    }

    override fun clearUserSecrets() {
        sharedPreferences
            .edit()
            .putString("user1", "")
            .putString("user2", "")
            .putString("score", "")
            .apply()
        GlobalScope.launch(Main) { isLoggedIn.value = false }
    }

    override fun getLoginScript(): String =
        """
            document.getElementById('UserName').value='${getUsername()}';
            document.getElementById('Password').value='${getPassword()}';
            document.getElementsByTagName('button')[0].click()
        """.trimIndent()

    override suspend fun updateScore() {
        if (score.value == null) loadFromCache()?.let { withContext(Dispatchers.Main) { score.value = it } }
        loadFromRemote()
    }
}