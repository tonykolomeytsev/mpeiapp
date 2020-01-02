package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.IsDarkThemeEnabledUseCase

class IsDarkThemeEnabledUseCaseImpl(
    private val userRepository: UserRepository
) : IsDarkThemeEnabledUseCase {
    override fun invoke() = userRepository.isDarkThemeEnabled
}