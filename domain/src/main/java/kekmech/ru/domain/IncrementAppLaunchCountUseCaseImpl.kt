package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.IncrementAppLaunchCountUseCase
import javax.inject.Inject

class IncrementAppLaunchCountUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : IncrementAppLaunchCountUseCase {
    override fun invoke() {
        userRepository.appLaunchCount++
    }
}