package kekmech.ru.core

import kekmech.ru.core.dto.User

interface UserRepository {
    fun get(refrash: Boolean = false): User
}