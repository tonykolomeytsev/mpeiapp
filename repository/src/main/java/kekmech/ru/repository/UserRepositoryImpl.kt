package kekmech.ru.repository

import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.dto.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    val cacheGateway: UserCacheGateway
) : UserRepository {

    override fun get(refrash: Boolean): User {
        return cacheGateway.get()
    }

}

