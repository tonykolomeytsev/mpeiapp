package kekmech.ru.domain_bars.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.redmadrobot.mapmemory.MapMemory
import com.redmadrobot.mapmemory.stateFlow
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_bars.data.mappers.RawToMarksResponseMapper
import kekmech.ru.domain_bars.data.mappers.RawToRatingResponseMapper
import kekmech.ru.domain_bars.dto.BarsUserInfo
import kekmech.ru.domain_bars.dto.RawMarksResponse
import kekmech.ru.domain_bars.dto.RawRatingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

interface BarsUserInfoRepository {

    var latestLoadedUrl: String

    fun observeBarsUserInfo(): Flow<BarsUserInfo>

    suspend fun pushMarksJson(marksJson: String)

    suspend fun pushStudentName(studentName: String)

    suspend fun pushStudentGroup(studentGroup: String)

    suspend fun pushStudentRating(ratingJson: String)
}

internal class BarsUserInfoRepositoryImpl(
    private val gson: Gson,
    mapMemory: MapMemory,
    preferences: SharedPreferences,
) : BarsUserInfoRepository {

    private val barsUserInfoCache by mapMemory.stateFlow(BarsUserInfo())
    override var latestLoadedUrl by preferences.string("bars_last_loaded_link")

    override fun observeBarsUserInfo(): Flow<BarsUserInfo> = barsUserInfoCache

    override suspend fun pushMarksJson(marksJson: String) {
        barsUserInfoCache.update { userBarsInfo ->
            val rawMarksResponse = gson.fromJson(marksJson, RawMarksResponse::class.java)
            userBarsInfo.copy(
                assessedDisciplines = RawToMarksResponseMapper.map(rawMarksResponse).payload,
            )
        }
    }

    override suspend fun pushStudentName(studentName: String) {
        barsUserInfoCache.update { userBarsInfo ->
            val studentNames = studentName.split("\\s+".toRegex())
            if (studentNames.size > 2) {
                userBarsInfo.copy(name = "${studentNames[0]} ${studentNames[1]}")
            } else {
                userBarsInfo.copy(name = studentName)
            }
        }
    }

    override suspend fun pushStudentGroup(studentGroup: String) {
        barsUserInfoCache.update { userBarsInfo ->
            userBarsInfo.copy(group = studentGroup)
        }
    }

    override suspend fun pushStudentRating(ratingJson: String) {
        barsUserInfoCache.update { userBarsInfo ->
            val rawRatingResponse = gson.fromJson(ratingJson, RawRatingResponse::class.java)
            userBarsInfo.copy(rating = RawToRatingResponseMapper.map(rawRatingResponse))
        }
    }
}
