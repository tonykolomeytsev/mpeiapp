package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteTransaction

interface SetCreateNoteTransactionUseCase {
    operator fun invoke(coupleNative: NoteTransaction)
}