package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.IsLoggedInBarsUseCase

class IsLoggedInBarsUseCaseImpl constructor(
    private val barsRepository: BarsRepository
) : IsLoggedInBarsUseCase {
    override fun invoke(): LiveData<Boolean> {
        return barsRepository.isLoggedIn
    }
}