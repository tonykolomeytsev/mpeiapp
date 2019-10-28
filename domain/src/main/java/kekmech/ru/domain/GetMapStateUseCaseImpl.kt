package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.GetMapStateUseCase
import javax.inject.Inject

class GetMapStateUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetMapStateUseCase {
    override fun invoke(): Int {
        return userRepository.mapState
    }
}