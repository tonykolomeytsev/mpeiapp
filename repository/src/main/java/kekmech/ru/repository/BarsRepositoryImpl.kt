package kekmech.ru.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import com.google.gson.GsonBuilder
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.repositories.BarsRepository.Companion.BARS_LIST_OF_STUDENTS
import kekmech.ru.core.repositories.BarsRepository.Companion.BARS_URL
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.utils.BarsParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class BarsRepositoryImpl constructor(
    private val context: Context,
    private val baseKeyStore: BaseKeyStore
) : BarsRepository {

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

    override val score = MutableLiveData<AcademicScore>()

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
                .method(Connection.Method.GET)
                .execute()
            val mainPageDocument = mainPage.parse()
            val stoken = mainPageDocument
                .select("input[name=SToken]")
                .`val`()
            val requestVirificationToken = mainPageDocument
                .select("input[name=__RequestVerificationToken]")
                .`val`()
            println(stoken)
            println(requestVirificationToken)

            val response = Jsoup.connect(BARS_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Origin", "https://bars.mpei.ru")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36 OPR/66.0.3515.60")
                .header("Sec-Fetch-User", "?1")
                .data("Password", getPassword())
                .data("Username", getUsername())
                .data("Remember", "false")
                .data("SToken", stoken)
                .data("__RequestVerificationToken", requestVirificationToken)
                .cookies(mainPage.cookies())
                .method(Connection.Method.POST)
                .execute()

            // если нас сразу редиректнуло на страницу ведомости
            if (response.url()?.path?.startsWith("/bars_web/Student/Part1") == true) {
                val barsParser = BarsParser()
                val score = barsParser.parse(response.parse())
                saveToCache(score)
                return score
            } else { // если зачетки две и нас редиректнуло на страничку выбора
                val listOfBars = Jsoup.connect(BARS_LIST_OF_STUDENTS)
                    .cookies(response.cookies())
                    .get()
                val href = getStudentUrl(listOfBars)
                val studentBarsPage = Jsoup.connect(href)
                    .cookies(response.cookies())
                    .get()

                val barsParser = BarsParser()
                val score = barsParser.parse(studentBarsPage)
                saveToCache(score)
                return score
            }
        } catch (e: BarsParser.LoginException) {
            // do nothing
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getStudentUrl(listOfBarsContent: Document): String {
        val trs = listOfBarsContent.select("table[id*=tbl__PartialListStudent]")
            .select("tbody")
            .select("tr")
        var maxYear = 0
        var maxYearStudentLink = ""
        // ищем максимальный год группы, чтобы открыть ссылку именно на последнюю группу обучения для этого студента
        trs.forEach { tr ->
            val tds = tr.select("td")
            val groupName = tds[2].html()
            val groupYear = (".*-.*-(.*)".toRegex().find(groupName)?.groups?.get(1)?.value ?: "0").toInt()
            if (groupYear > maxYear) {
                maxYear = groupYear
                maxYearStudentLink = "https://bars.mpei.ru" + tds[4].select("a").attr("href")
            }
        }
        return maxYearStudentLink
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
            .putString("user2", "") // PerezhilovaYD uxi762e
            .putString("score", "")
            .apply()
    }

    override suspend fun updateScore() {
        if (score.value == null) loadFromCache()?.let { withContext(Dispatchers.Main) { score.value = it } }
        loadFromRemote()
    }
}