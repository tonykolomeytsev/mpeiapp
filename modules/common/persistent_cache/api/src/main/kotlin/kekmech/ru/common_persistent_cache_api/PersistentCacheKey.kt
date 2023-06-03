package kekmech.ru.common_persistent_cache_api

import kotlin.reflect.KProperty

data class PersistentCacheKey(
    val name: String,
    val valueClass: Class<*>,
) {

    companion object {

        fun from(
            property: KProperty<*>,
            valueClass: Class<*>,
            salt: String?,
        ): PersistentCacheKey =
            PersistentCacheKey(
                name = "${property.name}_${salt.orEmpty()}_${valueClass.canonicalName}"
                    .map { if (it.isLetterOrDigit()) it else '_' }
                    .toCharArray()
                    .let(::String),
                valueClass = valueClass,
            )
    }
}
