package kekmech.ru.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetTomorrowCouplesUseCase

class GetTomorrowCouplesUseCaseImpl(
    private val scheduleRepository: ScheduleRepository
) : GetTomorrowCouplesUseCase {
    override operator fun invoke(): LiveData<List<CoupleNative>> = Transformations.map(scheduleRepository.schedule) { schedule ->
        if (schedule == null) return@map emptyList<CoupleNative>()
        // посмотрим в завтрашний день
        val tomorrow = Time.today().getDayWithOffset(1)
        val scheduleWeekNum = schedule.calendarWeek
        // если четность требуемой недели совпадает с чётностью
        // недели загрузки расписания, то в качестве нужных пар
        // принимаем пары отмеченные weekNum == 1
        val necessaryWeekNum = if (tomorrow.weekOfYear % 2 == scheduleWeekNum % 2) 1 else 2
        return@map schedule.coupleList.filter {
            (it.week == necessaryWeekNum) and (it.day == tomorrow.dayOfWeek)
        }
    }
}