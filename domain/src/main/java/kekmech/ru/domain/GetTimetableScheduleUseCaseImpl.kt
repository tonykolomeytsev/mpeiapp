package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetTimetableScheduleUseCase

class GetTimetableScheduleUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository
) : GetTimetableScheduleUseCase {

    override operator fun invoke(dayOfWeek: Int, weekNum: Int): List<CoupleNative> {
        val schedule = scheduleRepository.getSchedule(false) ?: return emptyList()
        val necessaryWeekNum = if (weekNum % 2 == schedule.calendarWeek % 2) 1 else 2
        val noteId = { num: Int -> notesRepository.getNoteFor(schedule.id, dayOfWeek, weekNum, num)?.id ?: -1 }
        return schedule.coupleList
            .filter { (it.day == dayOfWeek) and (it.week == necessaryWeekNum) }
            .onEach { it.noteId = noteId(it.num) }

    }
}