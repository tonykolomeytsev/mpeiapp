package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.SaveNoteUseCase
import javax.inject.Inject

class SaveNoteUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository,
    private val scheduleRepository: ScheduleRepository
) : SaveNoteUseCase {
    override fun invoke(note: NoteNative, isNoteEmpty: Boolean) {
        note.scheduleId = scheduleRepository.scheduleId
        notesRepository.saveNote(note, isNoteEmpty)
    }
}