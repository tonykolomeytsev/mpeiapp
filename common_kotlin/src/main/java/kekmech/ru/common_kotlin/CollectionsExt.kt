package kekmech.ru.common_kotlin

inline fun <T : Any> MutableList<T>.addIf(any: T, predicate: () -> Boolean) {
    if (predicate()) add(any)
}
