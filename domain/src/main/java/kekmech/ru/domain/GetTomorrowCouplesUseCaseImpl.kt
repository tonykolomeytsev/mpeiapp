package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.usecases.GetTomorrowCouplesUseCase

class GetTomorrowCouplesUseCaseImpl : GetTomorrowCouplesUseCase {
    override fun invoke(): List<CoupleNative> {

        return emptyList()
    }
}