package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.IsLoggedInBarsUseCase
import javax.inject.Inject

class IsLoggedInBarsUseCaseImpl @Inject constructor(
    private val barsRepository: BarsRepository
) : IsLoggedInBarsUseCase {
    override fun invoke(): Boolean {
        return barsRepository.isLoggedIn
    }
}