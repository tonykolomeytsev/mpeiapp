package kekmech.ru.core.usecases

import kekmech.ru.core.dto.User

@Deprecated("")
interface LoadUserInfoUseCase {
    fun execute(): User
}