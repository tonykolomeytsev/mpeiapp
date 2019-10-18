package kekmech.ru.core.repositories

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.DayStatus
import kekmech.ru.core.dto.Schedule

interface ScheduleRepository {
    fun getOffsetCouples(offset:Int, refresh: Boolean): List<CoupleNative>

    fun getSchedule(refresh: Boolean): Schedule?

    fun saveSchedule(schedule: Schedule)

    fun getGroupNum(): LiveData<String>
}