package kekmech.ru.app

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.auth.BaseKeyStoreV21
import kekmech.ru.repository.auth.BaseKeyStoreV23
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthTest {

    @Test
    fun testBaseKeyStoreV21() {
        val context = InstrumentationRegistry.getTargetContext()
        val ks: BaseKeyStore = BaseKeyStoreV21(context)
        ks.putAlias(DEFAULT_TEST_ALIAS)
        val sourceData = "tonykolomeytsev@gmail.com"
        val encrypted = ks.encrypt(sourceData)
        val decrypted = ks.decrypt(encrypted)
        assert(decrypted == sourceData)
    }

    @Test
    fun testBaseKeyStoreV23() {
        val ks: BaseKeyStore = BaseKeyStoreV23()
        ks.putAlias(DEFAULT_TEST_ALIAS)
        val sourceData = "tonykolomeytsev@gmail.com"
        val encrypted = ks.encrypt(sourceData)
        val decrypted = ks.decrypt(encrypted)
        assert(decrypted == sourceData)
    }

    companion object {
        const val DEFAULT_TEST_ALIAS = "abcdefghijklmnopqrstuvwxyz"
    }
}