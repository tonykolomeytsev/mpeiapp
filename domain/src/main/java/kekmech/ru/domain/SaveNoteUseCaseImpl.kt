package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.SaveNoteUseCase

class SaveNoteUseCaseImpl constructor(
    private val notesRepository: NotesRepository
) : SaveNoteUseCase {
    override fun invoke(note: NoteNative, isNoteEmpty: Boolean) {
        notesRepository.saveNote(note, isNoteEmpty)
    }
}