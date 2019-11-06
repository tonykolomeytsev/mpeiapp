package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.SaveUserSecretsUseCase
import javax.inject.Inject

class SaveUserSecretsUseCaseImpl @Inject constructor(
    private val barsRepository: BarsRepository
) : SaveUserSecretsUseCase {
    override fun invoke(login: String, pass: String) = barsRepository.saveUserSecrets(login, pass)
}