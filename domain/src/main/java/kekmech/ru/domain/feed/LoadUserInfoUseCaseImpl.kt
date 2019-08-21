package kekmech.ru.domain.feed

import kekmech.ru.core.dto.User
import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.LoadUserInfoUseCase
import kekmech.ru.domain.DaggerInteractorComponent
import javax.inject.Inject

class LoadUserInfoUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : LoadUserInfoUseCase {

    override fun execute(): User {
        return repository.get()
    }

}