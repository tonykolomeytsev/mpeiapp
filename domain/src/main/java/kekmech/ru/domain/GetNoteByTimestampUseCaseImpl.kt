package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetNoteByTimestampUseCase

class GetNoteByTimestampUseCaseImpl constructor(
    private val notesRepository: NotesRepository,
    private val scheduleRepository: ScheduleRepository
) : GetNoteByTimestampUseCase {
    override fun invoke(timestamp: String): NoteNative? =
        notesRepository.getNoteFor(scheduleRepository.schedule.value!!.id, timestamp)
}