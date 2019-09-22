package kekmech.ru.timetable.model

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.GetTimetableScheduleUseCase
import kekmech.ru.timetable.MinCoupleItem
import javax.inject.Inject

class TimetableFragmentModelImpl @Inject constructor(
    private val getTimetableScheduleUseCase: GetTimetableScheduleUseCase
) : TimetableFragmentModel {

    override val today: Time
        get() = Time.today()

    override fun getDaySchedule(dayOfWeek: Int, weekNum: Int): List<MinCoupleItem> {
        return getTimetableScheduleUseCase.execute(dayOfWeek, weekNum).map { MinCoupleItem(it) }
    }

}