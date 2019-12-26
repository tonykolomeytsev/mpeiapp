package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.GetAppLaunchCountUseCase

class GetAppLaunchCountUseCaseImpl constructor(
    private val userRepository: UserRepository
) : GetAppLaunchCountUseCase {
    override fun invoke(): Int {
        return userRepository.appLaunchCount
    }
}