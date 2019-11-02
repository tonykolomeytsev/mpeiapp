package kekmech.ru.core.repositories

import kekmech.ru.core.dto.AcademicScore

interface BarsRepository {

    suspend fun getScoreAsync(forceRefresh: Boolean): AcademicScore?

    companion object {
        const val BARS_URL = "https://bars.mpei.ru/bars_web/"
    }
}