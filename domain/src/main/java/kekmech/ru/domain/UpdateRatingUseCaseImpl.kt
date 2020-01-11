package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.UpdateRatingUseCase

class UpdateRatingUseCaseImpl(
    private val barsRepository: BarsRepository
) : UpdateRatingUseCase {
    override suspend fun invoke() {
        barsRepository.updateScore()
    }
}