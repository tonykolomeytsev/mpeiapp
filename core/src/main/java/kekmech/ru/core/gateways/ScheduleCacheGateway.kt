package kekmech.ru.core.gateways

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.WeekInfo

/**
 * Yes, it's just a another called repository
 */
interface ScheduleCacheGateway {
    fun getSchedule(): Schedule?

    fun getCouples(dayNum: Int, odd: Boolean): List<CoupleNative>?

    fun getWeekInfo(): WeekInfo?

    fun newSchedule(schedule: Schedule)
}