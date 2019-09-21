package kekmech.ru.core.gateways

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule

interface ScheduleRemoteGateway {
    fun getCouples(offset: Int): List<CoupleNative>?
    fun getSchedule(): Schedule?
    fun newSchedule(schedule: Schedule)
}