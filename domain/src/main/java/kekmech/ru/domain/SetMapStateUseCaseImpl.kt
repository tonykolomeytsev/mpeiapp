package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.SetMapStateUseCase

class SetMapStateUseCaseImpl constructor(
    private val userRepository: UserRepository
) : SetMapStateUseCase {
    override fun invoke(int: Int) {
        userRepository.mapState = int
    }
}