package kekmech.ru.repository

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.Time
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.repositories.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
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
        val offsetDay = today.getDayWithOffset(offset)
        if (today.dayOfWeek + offset <= 7) {
            val couples = scheduleCacheGateway.getCouples(today.dayOfWeek + offset, offsetDay.parity == Time.Parity.ODD)
            when {
                couples == null -> return emptyList()
                couples.isEmpty() -> return listOf(CoupleNative.dayOff)
                else -> return couples
            }
        } else {
            val necessaryDayOfWeek = (today.dayOfWeek + offset) % 7
            val couples = scheduleCacheGateway.getCouples(necessaryDayOfWeek, offsetDay.parity == Time.Parity.ODD)
            when {
                couples == null -> return emptyList()
                couples.isEmpty() -> return listOf(CoupleNative.dayOff)
                else -> return couples
            }
        }
    }

    override fun saveSchedule(schedule: Schedule) {
        scheduleCacheGateway.newSchedule(schedule)
    }

    override fun getGroupNum() = scheduleCacheGateway.getGroupNum()

}