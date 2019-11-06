package kekmech.ru.repository.utils

import android.util.Base64
import kekmech.ru.repository.BuildConfig
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoHandler {

    private var SecretKey = BuildConfig.AUTH_KEY
    private var IV = BuildConfig.AUTH_IV

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        InvalidKeyException::class,
        UnsupportedEncodingException::class,
        InvalidAlgorithmParameterException::class
    )
    fun encrypt(message: String): String {
        val srcBuff = message.toByteArray(charset("UTF8"))
        //here using substring because AES takes only 16 or 24 or 32 byte of key
        val skeySpec = SecretKeySpec(SecretKey.substring(0, 32).toByteArray(), "AES")
        val ivSpec = IvParameterSpec(IV.substring(0, 16).toByteArray())
        val ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec)
        val dstBuff = ecipher.doFinal(srcBuff)
        return Base64.encodeToString(dstBuff, Base64.DEFAULT)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        UnsupportedEncodingException::class
    )
    fun decrypt(encrypted: String): String {
        val skeySpec = SecretKeySpec(SecretKey.substring(0, 32).toByteArray(), "AES")
        val ivSpec = IvParameterSpec(IV.substring(0, 16).toByteArray())
        val ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec)
        val raw = Base64.decode(encrypted, Base64.DEFAULT)
        val originalBytes = ecipher.doFinal(raw)
        return String(originalBytes, Charset.forName("UTF-8"))
    }

    companion object {

        private var instance: CryptoHandler? = null

        fun getInstance(): CryptoHandler {

            if (instance == null) {
                instance = CryptoHandler()
            }
            return instance!!
        }
    }
}