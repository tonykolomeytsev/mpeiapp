package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.GetTomorrowCouplesUseCase

class GetTomorrowCouplesUseCaseImpl(
    private val scheduleRepository: OldScheduleRepository
) : GetTomorrowCouplesUseCase {
    override fun invoke(): List<CoupleNative> {
        // посмотрим в завтрашний день
        val tomorrow = Time.today().getDayWithOffset(1)
        val schedule = scheduleRepository.getSchedule(true) ?: return emptyList()
        val scheduleWeekNum = schedule.calendarWeek
        // если четность требуемой недели совпадает с чётностью
        // недели загрузки расписания, то в качестве нужных пар
        // принимаем пары отмеченные weekNum == 1
        val necessaryWeekNum = if (tomorrow.weekOfYear % 2 == scheduleWeekNum % 2) 1 else 2
        return schedule.coupleList.filter {
            (it.week == necessaryWeekNum) and (it.day == tomorrow.dayOfWeek)
        }
    }
}