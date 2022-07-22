package kekmech.ru.common_kotlin

fun<K, V> mutableLinkedHashMap(maxCapacity: Int) = object : LinkedHashMap<K, V>() {
    override fun removeEldestEntry(p0: MutableMap.MutableEntry<K, V>?) = size > maxCapacity
}