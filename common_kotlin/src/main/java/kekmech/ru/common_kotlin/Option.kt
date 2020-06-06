package kekmech.ru.common_kotlin

data class Option<T : Any>(
    val value: T?
) {
    val isEmpty = value == null
    val isNotEmpty = !isEmpty
}