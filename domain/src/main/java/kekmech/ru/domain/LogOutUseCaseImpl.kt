package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.LogOutUseCase
import javax.inject.Inject

class LogOutUseCaseImpl @Inject constructor(
    private val barsRepository: BarsRepository
): LogOutUseCase {
    override fun invoke() {
        barsRepository.clearUserSecrets()
    }
}