package kekmech.ru.feature_notes_impl.domain.usecase

import kekmech.ru.domain_schedule.data.ScheduleRepository
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_api.domain.usecase.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes_impl.data.repository.NotesRepository

internal class GetNotesForSelectedScheduleUseCaseImpl(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
) : GetNotesForSelectedScheduleUseCase {

    override suspend operator fun invoke(): List<Note> {
        val selectedSchedule = scheduleRepository.getSelectedSchedule()
        return notesRepository.getNotesBySchedule(selectedSchedule)
    }
}
