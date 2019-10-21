package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.GetForceUpdateDataUseCase
import javax.inject.Inject

class GetForceUpdateDataUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetForceUpdateDataUseCase {
    override fun invoke(): Pair<String, String> {
        return userRepository.savedUpdateUrl to userRepository.savedUpdateDescription
    }
}