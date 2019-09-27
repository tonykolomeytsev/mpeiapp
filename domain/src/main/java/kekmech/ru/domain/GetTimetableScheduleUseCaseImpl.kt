package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetTimetableScheduleUseCase
import javax.inject.Inject

class GetTimetableScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetTimetableScheduleUseCase {

    override operator fun invoke(dayOfWeek: Int, weekNum: Int): List<CoupleNative> {
        val schedule = scheduleRepository.getSchedule(false)
        val parity = if (weekNum % 2 == 0) CoupleNative.EVEN else CoupleNative.ODD
        return schedule.coupleList
            .filter { it.day == dayOfWeek }
            .filter { it.week == parity }
    }
}