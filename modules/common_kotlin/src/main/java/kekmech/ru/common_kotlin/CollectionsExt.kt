package kekmech.ru.common_kotlin

inline fun <T : Any> MutableList<T>.addIf(any: T, predicate: () -> Boolean) {
    if (predicate()) add(any)
}

operator fun <T : Any> List<T>.get(intRange: IntRange): List<T> {
    return subList(intRange.first, intRange.last)
}