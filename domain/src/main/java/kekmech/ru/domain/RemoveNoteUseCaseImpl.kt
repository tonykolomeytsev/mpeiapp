package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.RemoveNoteUseCase
import javax.inject.Inject

class RemoveNoteUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : RemoveNoteUseCase {
    override fun invoke(note: NoteNative) {
        notesRepository.removeNote(note)
    }
}