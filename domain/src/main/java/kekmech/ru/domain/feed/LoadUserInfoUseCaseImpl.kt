package kekmech.ru.domain.feed

import kekmech.ru.core.dto.User
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.LoadUserInfoUseCase
import javax.inject.Inject

@Deprecated("")
class LoadUserInfoUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : LoadUserInfoUseCase {

    override fun execute(): User {
        return repository.get()
    }

}