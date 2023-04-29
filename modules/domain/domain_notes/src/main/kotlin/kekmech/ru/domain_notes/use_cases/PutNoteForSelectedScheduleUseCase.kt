package kekmech.ru.domain_notes.use_cases

import io.reactivex.rxjava3.core.Completable
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.repository.ScheduleRepository

class PutNoteForSelectedScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
) {

    fun putNote(note: Note): Completable =
        scheduleRepository.getSelectedSchedule()
            .flatMapCompletable { notesRepository.updateOrInsertNoteBySchedule(it, note) }
}
