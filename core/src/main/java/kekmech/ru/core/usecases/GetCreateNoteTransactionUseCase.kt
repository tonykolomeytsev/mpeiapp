package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteTransaction

interface GetCreateNoteTransactionUseCase {
    operator fun invoke(): NoteTransaction?
}