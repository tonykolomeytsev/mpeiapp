package kekmech.ru.ext_kotlin

fun <T> fastLazy(inInitializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, inInitializer)
