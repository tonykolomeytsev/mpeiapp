package kekmech.ru.repository

import android.util.Log
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.core.repositories.ScheduleRepository
import java.util.*
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleRemoteGateway: ScheduleRemoteGateway,
    private val scheduleCacheGateway: ScheduleCacheGateway
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
        val dayOfWeek = today.get(Calendar.DAY_OF_WEEK) - 1
        if (dayOfWeek + offset <= 7) {
            if (refresh) return scheduleRemoteGateway.getCouples(dayOfWeek + offset) ?: emptyList()
            else return scheduleCacheGateway.getCouples(dayOfWeek + offset, true) ?: getOffsetCouples(offset, true)
        } else {
            val necessaryDayOfWeek = (dayOfWeek+offset) % 6
            if (refresh) return scheduleRemoteGateway.getCouples(necessaryDayOfWeek) ?: emptyList()
            else return scheduleCacheGateway.getCouples(necessaryDayOfWeek, true) ?: getOffsetCouples(offset, true)
        }
    }

}