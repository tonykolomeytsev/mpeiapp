package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative

interface GetTodayCouplesUseCase {
    operator fun invoke(): List<CoupleNative>
}