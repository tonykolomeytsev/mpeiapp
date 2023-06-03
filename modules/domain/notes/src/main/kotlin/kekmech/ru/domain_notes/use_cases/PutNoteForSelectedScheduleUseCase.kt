package kekmech.ru.domain_notes.use_cases

import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.data.ScheduleRepository

class PutNoteForSelectedScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
) {

    suspend operator fun invoke(note: Note) {
        val selectedSchedule = scheduleRepository.getSelectedSchedule()
        return notesRepository
            .updateOrInsertNoteBySchedule(
                selectedSchedule = selectedSchedule,
                note = note,
            )
    }
}
