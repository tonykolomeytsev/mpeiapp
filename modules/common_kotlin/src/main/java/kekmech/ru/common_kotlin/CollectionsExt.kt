package kekmech.ru.common_kotlin

inline fun <T : Any> MutableList<T>.addIf(any: T, predicate: () -> Boolean) {
    if (predicate()) add(any)
}

operator fun <T : Any> List<T>.get(intRange: IntRange): List<T> {
    return subList(intRange.first, intRange.last)
}

fun<K, V> mutableLinkedHashMap(maxCapacity: Int) = object : LinkedHashMap<K, V>() {
    override fun removeEldestEntry(p0: MutableMap.MutableEntry<K, V>?) = size > maxCapacity
}