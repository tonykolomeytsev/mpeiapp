package kekmech.ru.core.gateways

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.WeekInfo

interface ScheduleRemoteGateway {
    fun getCouples(offset: Int): List<CoupleNative>?
    fun getSchedule(): Schedule?
    fun newSchedule(schedule: Schedule)
}