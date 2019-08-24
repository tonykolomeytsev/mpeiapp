package kekmech.ru.core.repositories

import kekmech.ru.core.dto.User

interface UserRepository {
    fun get(refresh: Boolean = false): User
}