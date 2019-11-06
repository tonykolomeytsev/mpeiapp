package kekmech.ru.repository.auth

interface BaseKeyStore {
    fun encrypt(data: String): String
    fun decrypt(data: String): String
}