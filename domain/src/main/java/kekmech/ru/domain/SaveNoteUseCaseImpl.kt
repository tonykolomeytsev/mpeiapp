package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.SaveNoteUseCase
import javax.inject.Inject

class SaveNoteUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : SaveNoteUseCase {
    override fun invoke(note: NoteNative) {
        notesRepository.saveNote(note)
    }
}