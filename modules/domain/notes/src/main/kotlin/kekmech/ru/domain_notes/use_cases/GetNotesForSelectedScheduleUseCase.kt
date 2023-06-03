package kekmech.ru.domain_notes.use_cases

import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.data.ScheduleRepository

class GetNotesForSelectedScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
) {

    suspend operator fun invoke(): List<Note> {
        val selectedSchedule = scheduleRepository.getSelectedSchedule()
        return notesRepository.getNotesBySchedule(selectedSchedule)
    }
}
