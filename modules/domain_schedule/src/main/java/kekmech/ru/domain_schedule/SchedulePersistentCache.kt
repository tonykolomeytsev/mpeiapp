package kekmech.ru.domain_schedule

import android.content.Context
import com.google.gson.Gson
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_persistent_cache.GsonPersistentCache
import kekmech.ru.domain_schedule.dto.Schedule
import java.time.temporal.WeekFields
import java.util.*

class SchedulePersistentCache(
    context: Context,
    gson: Gson
) : GsonPersistentCache<Pair<String, Int>, Schedule>(
    context = context,
    gson = gson,
    cacheName = "schedule",
    valueClass = Schedule::class.java
) {

    override fun generateKeyForPut(key: Pair<String, Int>, value: Schedule?): String? {
        value ?: return null
        val (groupNumber, _) = key
        val weekOfYear = value.weeks.first().weekOfYear
        return "${groupNumber}_$weekOfYear"
    }

    override fun generateKeyForGet(key: Pair<String, Int>): String? {
        val (groupNumber, weekOffset) = key
        val weekOfYear = moscowLocalDate().get(WeekFields.of(Locale.ENGLISH).weekOfWeekBasedYear()) + weekOffset
        return "${groupNumber}_$weekOfYear"
    }
}