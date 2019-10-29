package kekmech.ru.repository.auth

interface BaseKeyStore {
    fun putAlias(alias: String)
    fun encrypt(data: String): String
    fun decrypt(data: String): String
}