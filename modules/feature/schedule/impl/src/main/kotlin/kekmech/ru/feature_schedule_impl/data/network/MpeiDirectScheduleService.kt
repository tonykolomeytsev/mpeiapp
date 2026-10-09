package kekmech.ru.feature_schedule_impl.data.network

import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_impl.data.mapper.MpeiScheduleMapper
import kekmech.ru.feature_schedule_impl.data.model.GetSearchResultsResponse
import kekmech.ru.feature_schedule_impl.data.model.ScheduleDto
import kekmech.ru.feature_schedule_impl.data.model.ScheduleTypeDto
import kekmech.ru.feature_schedule_impl.data.network.mpei.MpeiScheduleService
import timber.log.Timber
import kekmech.ru.lib_schedule.utils.atStartOfWeek
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap

internal class MpeiDirectScheduleService(
    private val mpeiService: MpeiScheduleService,
    private val legacyService: LegacyScheduleService,
) : ScheduleService {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private val idCache = ConcurrentHashMap<String, Long>()

    override suspend fun getSchedule(
        type: String,
        name: String,
        weekOffset: Int,
    ): ScheduleDto {
        return runCatching {
            fetchDirectSchedule(type, name, weekOffset)
        }.onFailure { e ->
            Timber.tag("MpeiDirectScheduleService").w(e, "Direct MPEI schedule fetch failed for $name, falling back to legacy")
        }.recoverCatching {
            legacyService.getSchedule(type, name, weekOffset)
        }.getOrThrow()
    }

    override suspend fun getSearchResults(
        query: String,
        type: ScheduleType?,
    ): GetSearchResultsResponse {
        return runCatching {
            val mpeiType = when (type) {
                ScheduleType.GROUP -> "group"
                ScheduleType.PERSON -> "person"
                null -> null
            }
            val results = mpeiService.search(term = query, type = mpeiType)
            // Cache IDs for fast subsequent getSchedule calls
            for (res in results) {
                idCache[res.label.trim().lowercase()] = res.id
            }
            val items = results.map { MpeiScheduleMapper.mapSearchResult(it) }
            GetSearchResultsResponse(items = items)
        }.onFailure { e ->
            Timber.tag("MpeiDirectScheduleService").w(e, "Direct MPEI search failed for $query, falling back to legacy")
        }.recoverCatching {
            legacyService.getSearchResults(query, type)
        }.getOrThrow()
    }

    private suspend fun fetchDirectSchedule(
        type: String,
        name: String,
        weekOffset: Int,
    ): ScheduleDto {
        val cacheKey = name.trim().lowercase()
        var id = idCache[cacheKey]

        if (id == null) {
            val searchResults = mpeiService.search(term = name, type = type)
            val match = searchResults.firstOrNull { it.label.equals(name.trim(), ignoreCase = true) }
                ?: searchResults.firstOrNull()
                ?: error("Schedule not found in MPEI database for name: $name")
            id = match.id
            idCache[cacheKey] = id
        }

        val monday = moscowLocalDate().atStartOfWeek().plusWeeks(weekOffset.toLong())
        val sunday = monday.plusDays(6)
        val startStr = monday.format(dateFormatter)
        val finishStr = sunday.format(dateFormatter)

        val mpeiType = if (type.equals("person", ignoreCase = true)) "person" else "group"
        val classesList = mpeiService.getSchedule(
            type = mpeiType,
            id = id,
            start = startStr,
            finish = finishStr,
        )

        val scheduleType = if (mpeiType == "person") {
            ScheduleTypeDto.PERSON
        } else {
            ScheduleTypeDto.GROUP
        }

        return MpeiScheduleMapper.mapToScheduleDto(
            name = name,
            id = id,
            type = scheduleType,
            monday = monday,
            classesList = classesList,
        )
    }
}
