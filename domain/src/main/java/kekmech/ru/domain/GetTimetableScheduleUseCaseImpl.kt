package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetTimetableScheduleUseCase
import javax.inject.Inject

class GetTimetableScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository
) : GetTimetableScheduleUseCase {

    override operator fun invoke(dayOfWeek: Int, weekNum: Int): List<CoupleNative> {
        val schedule = scheduleRepository.getSchedule(false)
        if (schedule == null) return emptyList()
        val noteId = { num: Int -> notesRepository.getNoteFor(schedule.id, dayOfWeek, weekNum, num)?.id ?: -1 }
        val parity = if (weekNum % 2 == 0) CoupleNative.EVEN else CoupleNative.ODD
        return schedule.coupleList
            .filter { (it.day == dayOfWeek) and (it.week == parity) }
            .onEach { it.noteId = noteId(it.num) }

    }
}