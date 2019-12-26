package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.GetMapStateUseCase

class GetMapStateUseCaseImpl constructor(
    private val userRepository: UserRepository
) : GetMapStateUseCase {
    override fun invoke(): Int {
        return userRepository.mapState
    }
}