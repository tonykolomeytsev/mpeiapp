package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.SaveUserSecretsUseCase

class SaveUserSecretsUseCaseImpl constructor(
    private val barsRepository: BarsRepository
) : SaveUserSecretsUseCase {
    override fun invoke(login: String, pass: String) = barsRepository.saveUserSecrets(login, pass)
}