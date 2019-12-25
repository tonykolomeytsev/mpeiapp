package kekmech.ru.repository.gateways

import kekmech.ru.core.dto.User
import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.repository.room.AppDatabase

class UserCacheGatewayImpl constructor(val appdb: AppDatabase) : UserCacheGateway {

    override fun get(): User? = appdb.userDao()
        .getAll()
        .firstOrNull()

    override fun set(user: User) {
        appdb.userDao()
            .update(user)
    }

}