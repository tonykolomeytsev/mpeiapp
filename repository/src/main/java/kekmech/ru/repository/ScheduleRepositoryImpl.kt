package kekmech.ru.repository

import android.util.Log
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.Time
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.core.repositories.ScheduleRepository
import java.util.*
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleRemoteGateway: ScheduleRemoteGateway,
    private val scheduleCacheGateway: ScheduleCacheGateway
) : ScheduleRepository {

    @Deprecated("Use Time.semesterWeek instead")
    override fun getCurrentWeek(refresh: Boolean): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSchedule(refresh: Boolean): Schedule {
        return scheduleCacheGateway.getSchedule()!!
    }

    override fun getOffsetCouples(offset: Int, refresh: Boolean): List<CoupleNative> {
        val today = Time.today()
        if (today.dayOfWeek + offset <= 7) {
            if (refresh) throw UnsupportedOperationException()
            else return scheduleCacheGateway.getCouples(today.dayOfWeek + offset, true) ?: getOffsetCouples(offset, true)
        } else {
            val necessaryDayOfWeek = (today.dayOfWeek + offset) % 7
            if (refresh) throw UnsupportedOperationException()
            else return scheduleCacheGateway.getCouples(necessaryDayOfWeek, true) ?: getOffsetCouples(offset, true)
        }
    }

    override fun saveSchedule(schedule: Schedule) {
        scheduleCacheGateway.newSchedule(schedule)
    }

    override fun getGroupNum() = scheduleCacheGateway.getGroupNum()

}