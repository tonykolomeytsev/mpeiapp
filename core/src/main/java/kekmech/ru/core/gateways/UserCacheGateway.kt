package kekmech.ru.core.gateways

import kekmech.ru.core.dto.User

interface UserCacheGateway {
    fun get(): User?
    fun set(user: User)
}