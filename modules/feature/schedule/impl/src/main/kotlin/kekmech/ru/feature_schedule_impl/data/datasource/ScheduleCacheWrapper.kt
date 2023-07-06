package kekmech.ru.feature_schedule_impl.data.datasource

import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey
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
        return PersistentCacheKey.from(
            name = "${name.uppercase()}_[$formattedDate]",
            valueClass = Schedule::class.java,
        )
    }

    private fun persistentKey(name: String, weekOffset: Int): PersistentCacheKey {
        val firstDayOfWeek = LocalDate.now()
            .plusWeeks(weekOffset.toLong())
            .with(DayOfWeek.MONDAY)
        val formattedDate = firstDayOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE)
        return PersistentCacheKey.from(
            name = "${name.uppercase()}_[$formattedDate]",
            valueClass = Schedule::class.java,
        )
    }
}
