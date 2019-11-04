package kekmech.ru.core.repositories

import kekmech.ru.core.dto.AcademicScore

interface BarsRepository {

    val isLoggedIn: Boolean

    suspend fun getScoreAsync(forceRefresh: Boolean): AcademicScore?

    fun saveUserSecrets(username: String, password: String)

    fun clearUserSecrets()

    companion object {
        const val BARS_URL = "https://bars.mpei.ru/bars_web/"
    }
}