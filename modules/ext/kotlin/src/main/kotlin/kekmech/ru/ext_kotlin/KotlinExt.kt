package kekmech.ru.ext_kotlin

public fun <T> fastLazy(inInitializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, inInitializer)
