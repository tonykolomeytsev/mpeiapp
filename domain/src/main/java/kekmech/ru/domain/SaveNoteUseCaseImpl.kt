package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.SaveNoteUseCase

class SaveNoteUseCaseImpl constructor(
    private val notesRepository: NotesRepository,
    private val scheduleRepository: OldScheduleRepository
) : SaveNoteUseCase {
    override fun invoke(note: NoteNative, isNoteEmpty: Boolean) {
        note.scheduleId = scheduleRepository.scheduleId
        notesRepository.saveNote(note, isNoteEmpty)
    }
}