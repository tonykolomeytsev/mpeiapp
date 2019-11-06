package kekmech.ru.app

import androidx.test.runner.AndroidJUnit4
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.auth.BaseKeyStoreV21
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthTest {

    @Test
    fun testBaseKeyStoreV21() {
        val ks: BaseKeyStore = BaseKeyStoreV21()
        val sourceData = "tonykolomeytsev@gmail.com"
        val encrypted = ks.encrypt(sourceData)
        val decrypted = ks.decrypt(encrypted)
        println("source=$sourceData, \nencrypted=$encrypted, \ndecrypted=$decrypted")
        assert(decrypted == sourceData)
    }

    companion object {
        const val DEFAULT_TEST_ALIAS = "abcdefghijklmnopqrstuvwxyz"
    }
}