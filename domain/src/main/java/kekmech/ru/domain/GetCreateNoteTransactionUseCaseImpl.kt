package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.GetCreateNoteTransactionUseCase
import javax.inject.Inject

class GetCreateNoteTransactionUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : GetCreateNoteTransactionUseCase {
    override fun invoke(): CoupleNative? {
        return notesRepository.noteCreationTransaction
    }
}