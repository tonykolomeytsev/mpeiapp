package kekmech.ru.common_kotlin

import java.util.concurrent.atomic.AtomicReference

fun <T> fastLazy(inInitializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, inInitializer)

fun <T> AtomicReference<T>.getOrPut(defaultValue: () -> T): T =
    get() ?: defaultValue().also { compareAndSet(null, it) }

fun String.uppercaseFirstChar() = replaceFirstChar { it.uppercaseChar() }