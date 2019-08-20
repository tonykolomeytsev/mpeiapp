package kekmech.ru.repository

import kekmech.ru.core.CacheGateway
import kekmech.ru.core.InternetGateway
import kekmech.ru.core.UserRepository
import kekmech.ru.core.dto.User

class UserRepositoryImpl(
    val internetGateway: InternetGateway,
    val cacheGateway: CacheGateway
) : UserRepository {

    override fun get(refrash: Boolean): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

