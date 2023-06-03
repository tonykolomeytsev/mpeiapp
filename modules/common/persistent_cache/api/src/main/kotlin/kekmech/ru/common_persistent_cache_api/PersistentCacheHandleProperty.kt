package kekmech.ru.common_persistent_cache_api

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("LocalVariableName")
class PersistentCacheHandleProperty<T>(
    initializer: (Any, KProperty<*>) -> T,
) : ReadOnlyProperty<Any, T> {

    private var initializer: ((Any, KProperty<*>) -> T)? = initializer
    private val lock = this
    private var _value: T? = null

    /**
     * Same implementation as [SynchronizedLazyImpl]
     */
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val _v1 = _value
        if (_v1 !== null) {
            return _v1
        }

        return synchronized(lock) {
            val _v2 = _value
            if (_v2 !== null) {
                _v2
            } else {
                val typedValue = initializer!!.invoke(thisRef, property)
                _value = typedValue
                initializer = null
                typedValue
            }
        }
    }
}
