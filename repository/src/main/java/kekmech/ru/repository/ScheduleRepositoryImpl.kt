package kekmech.ru.repository

import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.dto.CoupleNative
import java.util.*
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    val scheduleRemoteGateway: ScheduleRemoteGateway,
    val scheduleCacheGateway: ScheduleCacheGateway
) : ScheduleRepository {

    override fun get(offset: Int, refresh: Boolean): List<CoupleNative> {
        val today = Calendar.getInstance()
        today.time = Date(System.currentTimeMillis())
        val dayOfWeek = today.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek + offset <= 6) {
            return scheduleCacheGateway.get(dayOfWeek+offset, true)
        } else {
            val necessaryDayOfWeek = (dayOfWeek+offset) % 6
            return scheduleCacheGateway.get(necessaryDayOfWeek, true)
        }
    }

}