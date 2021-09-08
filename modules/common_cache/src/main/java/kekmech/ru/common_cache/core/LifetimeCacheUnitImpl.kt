package kekmech.ru.common_cache.core

@Deprecated("Deprecated in MpeiX v1.9.0")
class LifetimeCacheUnitImpl<T : Any>(
    private val key: String,
    private val lifetimeCache: LifetimeCache
) : LifetimeCacheUnit<T> {

    override fun set(value: T) = lifetimeCache.put(key, value)

    override fun asMaybe() = lifetimeCache.asMaybe<T>(key)

    override fun asObservable() = lifetimeCache.asObservable<T>(key)

    override fun clear() = lifetimeCache.remove(key)

    override fun getIfPresent() = lifetimeCache.getIfPresent<T>(key)

    override fun update(transform: (previous: T?) -> T?) = lifetimeCache.put(key, transform)

    override fun isNotEmpty() = lifetimeCache.contains(key)
}