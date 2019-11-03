package kekmech.ru.repository

import android.content.Context
import com.google.gson.GsonBuilder
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.repositories.BarsRepository.Companion.BARS_URL
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.utils.BarsParser
import org.jsoup.Jsoup
import javax.inject.Inject

class BarsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val baseKeyStore: BaseKeyStore
) : BarsRepository {

    private val sharedPreferences = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)

    override val isLoggedIn: Boolean
        get() = sharedPreferences.getString("user1", "")?.isNotEmpty() ?: false

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
            val mainPage = Jsoup.connect(BARS_URL)
                .get()
            val stoken = mainPage
                .select("input[name=SToken]")
                .`val`()
            val requestVirificationToken = mainPage
                .select("input[name=__RequestVerificationToken]")
                .`val`()
            println(stoken)
            println(requestVirificationToken)

            val response = Jsoup.connect(BARS_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .data("Password", getPassword())
                .data("Username", getUsername())
                .data("Remember", "false")
                .data("SToken", stoken)
                .data("__RequestVerificationToken", requestVirificationToken)
                .post()

            val barsParser = BarsParser()
            val score = barsParser.parse(response)
            saveToCache(score)
            return score
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun saveToCache(score: AcademicScore) {
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
}