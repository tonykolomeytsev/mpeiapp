package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.HasUserCredentialsUseCase

class HasUserCredentialsUseCaseImpl(
    private val barsRepository: BarsRepository
) : HasUserCredentialsUseCase {
    override operator fun invoke() = barsRepository.hasUserCredentials

}