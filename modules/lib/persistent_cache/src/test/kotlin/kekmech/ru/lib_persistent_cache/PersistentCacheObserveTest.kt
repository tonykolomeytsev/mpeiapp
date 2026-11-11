package kekmech.ru.lib_persistent_cache

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.lib_coroutines.CoroutineDispatchers
import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey
import kekmech.ru.lib_persistent_cache.impl.PersistentCacheImpl
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheDir
import kekmech.ru.lib_persistent_cache.impl.data.PersistentCacheSourceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.File
import java.nio.file.Files

/**
 * [PersistentCache.observe] with the real file storage and the real IO dispatcher,
 * like after the app restart: the value is only on the disk.
 */
internal class PersistentCacheObserveTest : StringSpec({

    val dispatchers = object : CoroutineDispatchers {
        override fun default(): CoroutineDispatcher = Dispatchers.Default
        override fun main(): CoroutineDispatcher = throw NotImplementedError()
        override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
        override fun io(): CoroutineDispatcher = Dispatchers.IO
    }
    val keyA = PersistentCacheKey.from(name = "a", valueClass = String::class.java)
    val keyB = PersistentCacheKey.from(name = "b", valueClass = String::class.java)

    fun withCacheDir(block: suspend (newCache: () -> PersistentCache) -> Unit) =
        Files.createTempDirectory("persistent-cache-observe").toFile().let { root ->
            try {
                runBlocking {
                    block {
                        val source = PersistentCacheSourceImpl(PersistentCacheDir(File(root, "persistent")))
                        PersistentCacheImpl(dispatchers, source)
                    }
                }
            } finally {
                root.deleteRecursively()
            }
        }

    "Observe emits the value saved before the restart" {
        withCacheDir { newCache ->
            newCache().save(keyA, "saved").isSuccess shouldBe true

            val restarted = newCache()
            withTimeout(TIMEOUT_MS) {
                restarted.observe<String>(keyA).first().value shouldBe "saved"
            }
        }
    }

    "Observe emits the saved value when another key is saved before the collection starts" {
        withCacheDir { newCache ->
            newCache().save(keyA, "saved").isSuccess shouldBe true

            val restarted = newCache()
            // The flow is created by one command and collected a bit later,
            // meanwhile another feature saves its own value
            val flowA = restarted.observe<String>(keyA)
            delay(DELAY_MS)
            restarted.save(keyB, "other")

            withTimeout(TIMEOUT_MS) {
                flowA.first().value shouldBe "saved"
            }
        }
    }

    "Observe emits the saved value, then the new one" {
        withCacheDir { newCache ->
            newCache().save(keyA, "old").isSuccess shouldBe true

            val restarted = newCache()
            coroutineScope {
                val values = async {
                    withTimeout(TIMEOUT_MS) {
                        restarted.observe<String>(keyA).map { it.value }.take(2).toList()
                    }
                }
                delay(DELAY_MS)
                restarted.save(keyA, "new")
                values.await() shouldContainExactly listOf("old", "new")
            }
        }
    }

    "Observe doesn't emit when there is no saved value" {
        withCacheDir { newCache ->
            val cache = newCache()
            coroutineScope {
                val first = async {
                    withTimeout(TIMEOUT_MS) { cache.observe<String>(keyA).first().value }
                }
                delay(DELAY_MS)
                cache.save(keyA, "first")
                first.await() shouldBe "first"
            }
        }
    }
}) {

    private companion object {
        const val TIMEOUT_MS = 2_000L
        const val DELAY_MS = 200L
    }
}
