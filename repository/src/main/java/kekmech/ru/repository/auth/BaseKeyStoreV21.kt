package kekmech.ru.repository.auth

import android.content.Context
import android.security.KeyPairGeneratorSpec
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.KeyPairGenerator
import javax.inject.Inject
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal


class BaseKeyStoreV21 @Inject constructor(
    private val context: Context
) : BaseKeyStore {
    private lateinit var alias: String

    override fun putAlias(alias: String) {
        this.alias = alias
    }

    private fun generateKey(): KeyStore.PrivateKeyEntry {
        val keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore.load(null)
        // Generate the RSA key pairs
        if (!keyStore.containsAlias(alias)) {
            // Generate a key pair for encryption
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            end.add(Calendar.YEAR, 30)
            val spec = KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(X500Principal("CN=$alias"))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build()
            val kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA, AndroidKeyStore)
            kpg.initialize(spec)
            kpg.generateKeyPair()
        }
        return keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
    }

    override fun encrypt(data: String): String {
        val privateKeyEntry = generateKey()
        // Encrypt the text
        val inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(data.toByteArray())
        cipherOutputStream.close()

        return String(outputStream.toByteArray())
    }

    override fun decrypt(data: String): String {
        val privateKeyEntry = generateKey()
        val output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
        val cipherInputStream = CipherInputStream(
            ByteArrayInputStream(data.toByteArray()), output
        )
        val values = mutableListOf<Byte>()
        var nextByte = cipherInputStream.read()
        while (nextByte != -1) {
            values.add(nextByte.toByte())
            nextByte = cipherInputStream.read()
        }

        val bytes = ByteArray(values.size)
        for (i in bytes.indices) {
            bytes[i] = values[i]
        }
        return String(bytes)
    }

    companion object {
        const val AndroidKeyStore = "AndroidKeyStore"
        const val KEY_ALGORITHM_RSA = "RSA"
        const val RSA_MODE =  "RSA/ECB/PKCS1Padding"
    }
}