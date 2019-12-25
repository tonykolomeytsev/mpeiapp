package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteTransaction
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.GetCreateNoteTransactionUseCase

class GetCreateNoteTransactionUseCaseImpl constructor(
    private val notesRepository: NotesRepository
) : GetCreateNoteTransactionUseCase {
    override fun invoke(): NoteTransaction? {
        return notesRepository.noteCreationTransaction
    }
}