package kekmech.ru.repository.gateways

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Inject

class ScheduleRemoteGatewayImpl @Inject constructor(appdb: AppDatabase) : ScheduleRemoteGateway {
    override fun getCouples(offset: Int): List<CoupleNative>? {
        return emptyList()
    }

    override fun getSchedule(): Schedule? {
        return null
    }

    override fun newSchedule(schedule: Schedule) {

    }
}