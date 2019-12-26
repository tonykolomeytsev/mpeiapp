package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.LogOutUseCase

class LogOutUseCaseImpl constructor(
    private val barsRepository: BarsRepository
): LogOutUseCase {
    override fun invoke() {
        barsRepository.clearUserSecrets()
    }
}