package kekmech.ru.repository.auth

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import kekmech.ru.repository.BuildConfig
import java.security.Key
import javax.inject.Inject
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec


@TargetApi(Build.VERSION_CODES.M)
class BaseKeyStoreV23 @Inject constructor() : BaseKeyStore {

    private lateinit var alias: String

    override fun putAlias(alias: String) {
        this.alias = alias
    }

    private fun generateKey(): Key {
        val keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore.load(null)

        if (!keyStore.containsAlias(alias)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(false) // if true, we must use c.iv (initial vector) in encrypt and decrypt function
                    .build()
            )
            keyGenerator.generateKey()
        }
        return keyStore.getKey(alias, null)
    }

    override fun encrypt(data: String): String {
        val c = Cipher.getInstance(AES_MODE)
        c.init(
            Cipher.ENCRYPT_MODE,
            generateKey(),
            GCMParameterSpec(128, null) // we must use c.iv (initial vector)
        )
        val encodedBytes = c.doFinal(data.toByteArray())
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
    }

    override fun decrypt(data: String): String {
        val c = Cipher.getInstance(AES_MODE)
        c.init(
            Cipher.DECRYPT_MODE,
            generateKey(),
            GCMParameterSpec(128, null) // we must use c.iv (initial vector)
        )
        return String(c.doFinal(data.toByteArray()))
    }

    companion object {
        const val AndroidKeyStore = "AndroidKeyStore"
        const val AES_MODE = "AES/GCM/NoPadding"
    }

}