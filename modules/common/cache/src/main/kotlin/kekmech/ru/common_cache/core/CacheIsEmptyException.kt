package kekmech.ru.common_cache.core

class CacheIsEmptyException(key: String) : RuntimeException("Value is empty for key $key")
