package kekmech.ru.core.repositories

import kekmech.ru.core.dto.User

interface UserRepository {
    var savedUpdateUrl: String
    var savedUpdateDescription: String

    fun get(refresh: Boolean = false): User
}