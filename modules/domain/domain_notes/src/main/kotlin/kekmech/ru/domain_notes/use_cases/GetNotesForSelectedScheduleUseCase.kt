package kekmech.ru.domain_notes.use_cases

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.repository.ScheduleRepository

public class GetNotesForSelectedScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
) {

    public fun getNotes(): Single<List<Note>> =
        scheduleRepository.getSelectedSchedule()
            .flatMap { notesRepository.getNotesBySchedule(it) }
}
