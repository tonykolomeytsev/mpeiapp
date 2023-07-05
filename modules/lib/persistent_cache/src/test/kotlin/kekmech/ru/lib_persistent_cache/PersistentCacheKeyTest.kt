package kekmech.ru.lib_persistent_cache

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.string.shouldMatch
import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey

internal class PersistentCacheKeyTest : StringSpec({

    "Test PersistentCacheKey equality" {
        val key1 = PersistentCacheKey.from(
            name = "Hello world",
            valueClass = ArrayList::class.java,
        )
        val key2 = PersistentCacheKey.from(
            name = "Hello world",
            valueClass = ArrayList::class.java,
        )
        val key3 = PersistentCacheKey.from(
            name = "Hello world",
            valueClass = ArrayList::class.java,
        )
        key1 shouldBeEqual key2
        key2 shouldBeEqual key1

        key2 shouldBeEqual key3
        key3 shouldBeEqual key2

        key1 shouldBeEqual key3
        key3 shouldBeEqual key1
    }

    "Test PersistentCacheKey inequality" {
        val key1 = PersistentCacheKey.from(
            name = "Hello world",
            valueClass = ArrayList::class.java,
        )
        val key2 = PersistentCacheKey.from(
            name = "World hello",
            valueClass = ArrayList::class.java,
        )
        val key3 = PersistentCacheKey.from(
            name = "Hello world",
            valueClass = String::class.java,
        )
        key1 shouldNotBeEqual key2
        key2 shouldNotBeEqual key1

        key2 shouldNotBeEqual key3
        key3 shouldNotBeEqual key2

        key1 shouldNotBeEqual key3
        key3 shouldNotBeEqual key1
    }

    "Test PersistentCacheKey contains only allowed symbols" {
        val key = PersistentCacheKey.from(
            name = "eby.907-25@zbock.com/abc\\def",
            valueClass = String::class.java,
        )
        key.name shouldMatch """[0-9A-Za-z_]+""".toRegex()
    }
})
