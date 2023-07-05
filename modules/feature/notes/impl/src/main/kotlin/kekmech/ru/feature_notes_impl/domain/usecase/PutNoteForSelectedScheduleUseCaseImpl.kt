package kekmech.ru.feature_notes_impl.domain.usecase

import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_api.domain.usecase.PutNoteForSelectedScheduleUseCase
import kekmech.ru.feature_notes_impl.data.repository.NotesRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository

internal class PutNoteForSelectedScheduleUseCaseImpl(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
) : PutNoteForSelectedScheduleUseCase {

    override suspend operator fun invoke(note: Note) {
        val selectedSchedule = scheduleRepository.getSelectedSchedule()
        return notesRepository
            .updateOrInsertNoteBySchedule(
                selectedSchedule = selectedSchedule,
                note = note,
            )
    }
}
