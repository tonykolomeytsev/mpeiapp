package kekmech.ru.feature_bars_impl.data.repository

import android.content.SharedPreferences
import kekmech.ru.ext_shared_preferences.string
import kekmech.ru.feature_bars_impl.data.model.RawMarksResponse
import kekmech.ru.feature_bars_impl.data.model.RawRatingResponse
import kekmech.ru.feature_bars_impl.data.mapper.RawToMarksResponseMapper
import kekmech.ru.feature_bars_impl.data.mapper.RawToRatingResponseMapper
import kekmech.ru.feature_bars_impl.domain.BarsUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

internal class BarsUserInfoRepository(
    private val json: Json,
    preferences: SharedPreferences,
) {

    private val barsUserInfoCache = MutableStateFlow(BarsUserInfo())
    var latestLoadedUrl by preferences.string("bars_last_loaded_link")

    fun observeBarsUserInfo(): Flow<BarsUserInfo> = barsUserInfoCache

    suspend fun pushMarksJson(marksJson: String) {
        barsUserInfoCache.update { userBarsInfo ->
            val rawMarksResponse = json.decodeFromString<RawMarksResponse>(marksJson)
            userBarsInfo.copy(
                assessedDisciplines = RawToMarksResponseMapper.map(rawMarksResponse).payload,
            )
        }
    }

    suspend fun pushStudentName(studentName: String) {
        barsUserInfoCache.update { userBarsInfo ->
            val studentNames = studentName.split("\\s+".toRegex())
            if (studentNames.size > 2) {
                userBarsInfo.copy(name = "${studentNames[0]} ${studentNames[1]}")
            } else {
                userBarsInfo.copy(name = studentName)
            }
        }
    }

    suspend fun pushStudentGroup(studentGroup: String) {
        barsUserInfoCache.update { userBarsInfo ->
            userBarsInfo.copy(group = studentGroup)
        }
    }

    suspend fun pushStudentRating(ratingJson: String) {
        barsUserInfoCache.update { userBarsInfo ->
            val rawRatingResponse = json.decodeFromString<RawRatingResponse>(ratingJson)
            userBarsInfo.copy(rating = RawToRatingResponseMapper.map(rawRatingResponse))
        }
    }
}
