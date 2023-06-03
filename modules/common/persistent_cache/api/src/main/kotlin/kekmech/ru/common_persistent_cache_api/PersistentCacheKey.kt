package kekmech.ru.common_persistent_cache_api

import kotlin.reflect.KProperty

data class PersistentCacheKey(
    val name: String,
    val valueClass: Class<*>,
) {

    companion object {

        fun from(
            prefix: String?,
            property: KProperty<*>,
            valueClass: Class<*>,
        ): PersistentCacheKey =
            PersistentCacheKey(
                name = "${prefix.orEmpty()}_${property.name}_${valueClass.canonicalName}"
                    .map { if (it.isLetterOrDigit()) it else '_' }
                    .toCharArray()
                    .let(::String),
                valueClass = valueClass,
            )
    }
}
