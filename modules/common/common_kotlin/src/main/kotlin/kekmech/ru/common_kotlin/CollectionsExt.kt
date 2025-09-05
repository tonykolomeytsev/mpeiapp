package kekmech.ru.common_kotlin

public fun<K, V> mutableLinkedHashMap(maxCapacity: Int): LinkedHashMap<K, V> = object : LinkedHashMap<K, V>() {
    override fun removeEldestEntry(p0: MutableMap.MutableEntry<K, V>?) = size > maxCapacity
}
