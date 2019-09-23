package kekmech.ru.timetable.model

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.GetTimetableScheduleUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.MinCoupleItem
import kekmech.ru.timetable.MinLunchItem
import javax.inject.Inject

class TimetableFragmentModelImpl @Inject constructor(
    private val getTimetableScheduleUseCase: GetTimetableScheduleUseCase
) : TimetableFragmentModel {

    override val today: Time
        get() = Time.today()

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