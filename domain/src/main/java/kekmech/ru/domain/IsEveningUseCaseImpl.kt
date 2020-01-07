package kekmech.ru.domain

import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.IsEveningUseCase

class IsEveningUseCaseImpl : IsEveningUseCase {
    override fun invoke(): Boolean {
        val today = Time()
        return today.hours24 in (17 until 24)
    }
}