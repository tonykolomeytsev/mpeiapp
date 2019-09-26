package kekmech.ru.timetable.model

import android.content.Context
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.GetTimetableScheduleUseCase
import kekmech.ru.core.usecases.LoadDayStatusUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.R
import kekmech.ru.timetable.view.items.MinCoupleItem
import kekmech.ru.timetable.view.items.MinLunchItem
import java.util.*
import javax.inject.Inject

class TimetableFragmentModelImpl @Inject constructor(
    private val getTimetableScheduleUseCase: GetTimetableScheduleUseCase,
    private val loadDayStatusUseCase: LoadDayStatusUseCase,
    private val context: Context
) : TimetableFragmentModel {

    override val today: Time
        get() = Time.today()

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: String
        get() = loadDayStatusUseCase.execute(0).groupNum.toUpperCase(Locale.getDefault())

    /**
     * Current week number
     */
    override val currentWeekNumber: Int
        get() = today.weekOfSemester

    override val formattedTodayStatus: String
        get() = "${today.formattedAsDayName(context, R.array.days_of_week)}, ${today.dayOfMonth} " +
                today.formattedAsMonthName(context, R.array.months)

    override fun getDaySchedule(dayOfWeek: Int, weekNum: Int): List<BaseItem<*>> {
        val couples: MutableList<BaseItem<*>> = getTimetableScheduleUseCase.execute(dayOfWeek, weekNum)
            .map { MinCoupleItem(it) }
            .toMutableList()
        // insert lunch
        val thirdCoupleIndex = couples
            .indexOfFirst { (it as MinCoupleItem).coupleNative.num == 3 }
        if (thirdCoupleIndex != -1 && (couples.first() as MinCoupleItem).coupleNative.num != 3)
            couples.add(thirdCoupleIndex, MinLunchItem())
        return couples
    }

}