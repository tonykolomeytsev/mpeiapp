package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.SetForceUpdateDataUseCase

class SetForceUpdateDataUseCaseImpl constructor(
    private val userRepository: UserRepository
) : SetForceUpdateDataUseCase {
    override fun invoke(url: String, description: String) {
        userRepository.savedUpdateDescription = description
        userRepository.savedUpdateUrl = url
    }
}