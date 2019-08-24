package kekmech.ru.repository

import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.dto.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userCacheGateway: UserCacheGateway
) : UserRepository {

    override fun get(refresh: Boolean): User {
        val user = userCacheGateway.get()
        if (user == null) userCacheGateway.set(User.defaultUser())
        return userCacheGateway.get()!!
    }

}

