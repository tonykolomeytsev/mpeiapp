package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetTodayCouplesUseCase

class GetTodayCouplesUseCaseImpl(
    private val scheduleRepository: ScheduleRepository
) : GetTodayCouplesUseCase {
    override fun invoke(): List<CoupleNative> {
        // посмотрим в завтрашний день
        val today = Time.today()
        val schedule = scheduleRepository.getSchedule(true) ?: return emptyList()
        val scheduleWeekNum = schedule.calendarWeek
        // если четность требуемой недели совпадает с чётностью
        // недели загрузки расписания, то в качестве нужных пар
        // принимаем пары отмеченные weekNum == 1
        val necessaryWeekNum = if (today.weekOfYear % 2 == scheduleWeekNum % 2) 1 else 2
        return schedule.coupleList.filter {
            (it.week == necessaryWeekNum) and (it.day == today.dayOfWeek)
        }
    }
}