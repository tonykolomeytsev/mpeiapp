package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.SetMapStateUseCase
import javax.inject.Inject

class SetMapStateUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : SetMapStateUseCase {
    override fun invoke(int: Int) {
        userRepository.mapState = int
    }
}