package kekmech.ru.core.usecases

import kekmech.ru.core.dto.User

interface LoadUserInfoUseCase {
    fun execute(): User
}