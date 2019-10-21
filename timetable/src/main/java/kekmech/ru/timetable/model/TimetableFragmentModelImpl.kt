package kekmech.ru.timetable.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.GetGroupNumberUseCase
import kekmech.ru.core.usecases.GetTimetableScheduleUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.R
import kekmech.ru.timetable.view.items.MinCoupleItem
import kekmech.ru.timetable.view.items.MinLunchItem
import java.util.*
import javax.inject.Inject

class TimetableFragmentModelImpl @Inject constructor(
    private val getTimetableScheduleUseCase: GetTimetableScheduleUseCase,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
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

}