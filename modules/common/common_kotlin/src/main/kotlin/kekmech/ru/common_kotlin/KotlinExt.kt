package kekmech.ru.common_kotlin

public fun <T> fastLazy(inInitializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, inInitializer)
