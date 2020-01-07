package kekmech.ru.timetable.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteTransaction
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.*
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.R
import kekmech.ru.timetable.view.items.MinCoupleItem
import kekmech.ru.timetable.view.items.MinLunchItem
import java.util.*

class TimetableFragmentModelImpl constructor(
    private val getTimetableScheduleUseCase: GetTimetableScheduleUseCase,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val setIsShowedUpdateDialogUseCase: SetIsShowedUpdateDialogUseCase,
    private val getIsShowedUpdateDialogUseCase: GetIsShowedUpdateDialogUseCase,
    private val setForceUpdateDataUseCase: SetForceUpdateDataUseCase,
    private val setCreateNoteTransactionUseCase: SetCreateNoteTransactionUseCase,
    private val invokeUpdateScheduleUseCase: InvokeUpdateScheduleUseCase,
    private val context: Context
) : TimetableFragmentModel {

    override val today: Time
        get() = Time.today()

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: LiveData<String> get() = getGroupNumberUseCase()

    /**
     * Current week number
     */
    override val currentWeekNumber: Int
        get() = today.weekOfSemester

    override val formattedTodayStatus: String
        get() = "${today.formattedAsDayName(context, R.array.days_of_week)}, ${today.dayOfMonth} " +
                today.formattedAsMonthName(context, R.array.months)


    override var isNotShowedUpdateDialog: Boolean
        get() = getIsShowedUpdateDialogUseCase()
        set(value) { setIsShowedUpdateDialogUseCase(value) }

    override fun getDaySchedule(dayOfWeek: Int, weekNum: Int): List<BaseItem<*>> {
        val couples: MutableList<BaseItem<*>> = getTimetableScheduleUseCase(dayOfWeek, weekNum)
            .map { MinCoupleItem(it) }
            .toMutableList()
        // insert lunch
        val thirdCoupleIndex = couples
            .indexOfFirst { (it as MinCoupleItem).coupleNative.num == 3 }
        if (thirdCoupleIndex != -1 && (couples.first() as MinCoupleItem).coupleNative.num != 3)
            couples.add(thirdCoupleIndex, MinLunchItem())
        return couples
    }

    override var weekOffset: LiveData<Int> = MutableLiveData<Int>().apply { value = 0 }

    override fun saveForceUpdateArgs(url: String, description: String) {
        setForceUpdateDataUseCase(url, description)
    }

    override fun transactCouple(coupleNative: CoupleNative) {
        setCreateNoteTransactionUseCase(NoteTransaction(coupleNative, currentWeekNumber + (weekOffset.value ?: 0)))
    }

    override suspend fun updateScheduleFromRemote() {
        try {
            invokeUpdateScheduleUseCase()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override var selectedPage: Int = 0
}