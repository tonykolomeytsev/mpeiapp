package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.SetCreateNoteTransactionUseCase
import javax.inject.Inject

class SetCreateNoteTransactionUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : SetCreateNoteTransactionUseCase {
    override fun invoke(coupleNative: CoupleNative) {
        notesRepository.noteCreationTransaction = coupleNative
    }
}