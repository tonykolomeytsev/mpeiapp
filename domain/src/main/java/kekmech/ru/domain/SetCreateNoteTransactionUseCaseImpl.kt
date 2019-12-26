package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteTransaction
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.SetCreateNoteTransactionUseCase

class SetCreateNoteTransactionUseCaseImpl constructor(
    private val notesRepository: NotesRepository
) : SetCreateNoteTransactionUseCase {
    override fun invoke(coupleNative: NoteTransaction) {
        notesRepository.noteCreationTransaction = coupleNative
    }
}