package kekmech.ru.common_persistent_cache_impl

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import kekmech.ru.common_coroutines_api.CoroutineDispatchers
import kekmech.ru.common_persistent_cache_api.PersistentCacheKey
import kekmech.ru.common_persistent_cache_api.of
import kekmech.ru.common_persistent_cache_api.ofList
import kekmech.ru.common_persistent_cache_impl.data.PersistentCacheSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

internal class PersistentCacheImplTest : StringSpec({

    fun mockPersistentCacheSource(): PersistentCacheSource =
        object : PersistentCacheSource {

            private var bytes: ByteArrayOutputStream = ByteArrayOutputStream()

            override fun inputStream(key: PersistentCacheKey): InputStream =
                bytes.toByteArray().inputStream()

            override fun outputStream(key: PersistentCacheKey): OutputStream = bytes

            override fun delete(key: PersistentCacheKey) {
                bytes.reset()
            }
        }

    fun mockCoroutineDispatchers(): CoroutineDispatchers =
        object : CoroutineDispatchers {
            override fun default(): CoroutineDispatcher = throw NotImplementedError()

            override fun main(): CoroutineDispatcher = throw NotImplementedError()

            override fun unconfined(): CoroutineDispatcher = throw NotImplementedError()

            @OptIn(ExperimentalCoroutinesApi::class)
            override fun io(): CoroutineDispatcher = UnconfinedTestDispatcher()
        }

    data class Student(
        val name: String,
        val age: UInt,
        val university: String,
        val program: String,
    ) : Serializable

    "Test data is intact after serialization and deserialization" {
        val persistentCache = PersistentCacheImpl(
            dispatchers = mockCoroutineDispatchers(),
            source = mockPersistentCacheSource(),
        )
        val key = PersistentCacheKey.from(
            name = "12345!@#$%_hello_WORLD",
            valueClass = Student::class.java,
        )
        val sourceValue = Student(
            name = "Igor Emelyanov",
            age = 22u,
            university = "Skolkek",
            program = "MSc",
        )

        persistentCache.save(key, sourceValue).getOrThrow()
        val newValue = persistentCache.restore<Student>(key).getOrThrow()

        sourceValue shouldBeEqual newValue
    }

    "Test single value accessor" {
        val persistentCache = PersistentCacheImpl(
            dispatchers = mockCoroutineDispatchers(),
            source = mockPersistentCacheSource(),
        )
        val sourceValue = Student(
            name = "Igor Emelyanov",
            age = 22u,
            university = "Skolkek",
            program = "MSc",
        )

        val owner = object { val accessor by persistentCache.of<Student>() }
        owner.accessor.put(sourceValue).getOrThrow()
        val newValue = owner.accessor.get().getOrThrow()

        sourceValue shouldBeEqual newValue
    }

    "Test list of values accessor" {
        val persistentCache = PersistentCacheImpl(
            dispatchers = mockCoroutineDispatchers(),
            source = mockPersistentCacheSource(),
        )
        val sourceValue1 = Student(
            name = "Igor Emelyanov",
            age = 22u,
            university = "Skolkek",
            program = "MSc",
        )
        val sourceValue2 = Student(
            name = "Vladislav Lapin",
            age = 21u,
            university = "Bomonka",
            program = "BSc",
        )
        val valueList = listOf(sourceValue1, sourceValue2)

        val owner = object { val accessor by persistentCache.ofList<Student>() }
        owner.accessor.put(valueList).getOrThrow()
        val newList = owner.accessor.get().getOrThrow()

        valueList shouldContainExactly newList
    }
})