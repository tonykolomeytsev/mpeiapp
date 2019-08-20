package kekmech.ru.core.repositories

import kekmech.ru.core.dto.User

interface UserRepository {
    fun get(refrash: Boolean = false): User
}