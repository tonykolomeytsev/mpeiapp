package kekmech.ru.domain_schedule.data

import kekmech.ru.common_persistent_cache_api.PersistentCache
import kekmech.ru.common_persistent_cache_api.PersistentCacheKey
import kekmech.ru.domain_schedule_models.dto.Schedule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class ScheduleCacheWrapper(
    private val persistentCache: PersistentCache,
) {

    suspend fun save(schedule: Schedule): Result<Unit> =
        persistentCache.save(
            key = schedule.persistentKey(),
            value = schedule,
        )

    suspend fun restore(name: String, weekOffset: Int): Result<Schedule> =
        persistentCache.restore(
            persistentKey(
                name = name,
                weekOffset = weekOffset,
            ),
        )

    private fun Schedule.persistentKey(): PersistentCacheKey {
        val formattedDate = weeks.first().firstDayOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE)
        return PersistentCacheKey(
            name = "${name.uppercase()}_[$formattedDate]",
            valueClass = Schedule::class.java,
        )
    }

    private fun persistentKey(name: String, weekOffset: Int): PersistentCacheKey {
        val firstDayOfWeek = LocalDate.now()
            .plusWeeks(weekOffset.toLong())
            .with(DayOfWeek.MONDAY)
        val formattedDate = firstDayOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE)
        return PersistentCacheKey(
            name = "${name.uppercase()}_[$formattedDate]",
            valueClass = Schedule::class.java,
        )
    }
}