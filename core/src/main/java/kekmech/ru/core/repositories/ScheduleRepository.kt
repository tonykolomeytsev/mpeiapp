package kekmech.ru.core.repositories

import kekmech.ru.core.dto.Couple
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.WeekInfo

interface ScheduleRepository {
    fun getOffsetCouples(offset:Int, refresh: Boolean): List<CoupleNative>

    fun getCurrentWeek(refresh: Boolean): Int

    fun getSchedule(refresh: Boolean): Schedule
}