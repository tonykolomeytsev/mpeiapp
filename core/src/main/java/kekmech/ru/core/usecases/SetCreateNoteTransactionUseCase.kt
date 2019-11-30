package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative

interface SetCreateNoteTransactionUseCase {
    operator fun invoke(coupleNative: CoupleNative)
}