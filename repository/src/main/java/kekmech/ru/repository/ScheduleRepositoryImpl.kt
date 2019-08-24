package kekmech.ru.repository

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.core.repositories.ScheduleRepository
import java.util.*
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    val scheduleRemoteGateway: ScheduleRemoteGateway,
    val scheduleCacheGateway: ScheduleCacheGateway
) : ScheduleRepository {
    override fun getCurrentWeek(refresh: Boolean): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSchedule(refresh: Boolean): Schedule {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOffsetCouples(offset: Int, refresh: Boolean): List<CoupleNative> {
        val today = Calendar.getInstance()
        today.time = Date(System.currentTimeMillis())
        val dayOfWeek = today.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek + offset <= 6) {
            return scheduleCacheGateway.getCouples(dayOfWeek + offset, true) ?: getOffsetCouples(offset, true)
        } else {
            val necessaryDayOfWeek = (dayOfWeek+offset) % 6
            return scheduleCacheGateway.getCouples(necessaryDayOfWeek, true) ?: getOffsetCouples(offset, true)
        }
    }

}