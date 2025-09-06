package kekmech.ru.lib_persistent_cache.api

import kekmech.ru.lib_persistent_cache.api.PersistentCacheKey.Companion.from
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * A key intended for accessing persistent cache items.
 * Collision protection is the developer's responsibility.
 *
 * Use factory method [from] to create your own key instance.
 */
public class PersistentCacheKey private constructor(
    internal val name: String,
    internal val valueClass: Class<*>,
) {

    public fun filename(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersistentCacheKey

        if (name != other.name) return false
        if (valueClass != other.valueClass) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + valueClass.hashCode()
        return result
    }


    public companion object {

        public fun <T : Serializable> from(name: String, valueClass: Class<T>): PersistentCacheKey =
            PersistentCacheKey(
                name = name.normalize(),
                valueClass = valueClass,
            )

        public fun <T : Serializable> from(
            prefix: String?,
            property: KProperty<*>,
            valueClass: Class<T>,
        ): PersistentCacheKey =
            from(
                name = "${prefix.orEmpty()}_${property.name}_${valueClass.canonicalName}",
                valueClass = valueClass,
            )

        private fun String.normalize() =
            map { if (it.isLetterOrDigit()) it else '_' }
                .toCharArray()
                .let(::String)
    }
}
