package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative

interface GetCreateNoteTransactionUseCase {
    operator fun invoke(): CoupleNative?
}