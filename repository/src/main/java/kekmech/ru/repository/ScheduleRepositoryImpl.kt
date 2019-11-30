package kekmech.ru.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.*
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.repositories.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleCacheGateway: ScheduleCacheGateway
) : ScheduleRepository {

    override var isNeedToUpdateFeed = MutableLiveData<Boolean>().apply { value = false }

    override val scheduleId: Int get() = scheduleCacheGateway.scheduleId

    override fun getSchedule(refresh: Boolean): Schedule? {
        return scheduleCacheGateway.getSchedule()
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

    private val groupNumber = MutableLiveData<String>().apply { value = "" }
    override fun getGroupNum(): LiveData<String> {
        GlobalScope.launch(Dispatchers.Main) {
            groupNumber.value = withContext(Dispatchers.IO) {
                scheduleCacheGateway.getGroupNum().toUpperCase(Locale.getDefault())
            }
        }
        return groupNumber
    }

    override fun getAllSchedules() = scheduleCacheGateway.getAllSchedules()

    override fun setCurrentScheduleId(id: Int) = scheduleCacheGateway.setCurrentScheduleId(id)

}