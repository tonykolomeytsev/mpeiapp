package kekmech.ru.core.repositories

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule

interface ScheduleRepository {
    fun getOffsetCouples(offset:Int, refresh: Boolean): List<CoupleNative>

    fun getCurrentWeek(refresh: Boolean): Int

    fun getSchedule(refresh: Boolean): Schedule

    fun saveSchedule(schedule: Schedule)
}