package kekmech.ru.repository.auth

import kekmech.ru.repository.utils.CryptoHandler

class BaseKeyStoreV21 : BaseKeyStore {
    private val cryptoHandler = CryptoHandler.getInstance()

    override fun encrypt(data: String) = cryptoHandler.encrypt(data)

    override fun decrypt(data: String) = cryptoHandler.decrypt(data)
}