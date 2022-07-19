package kekmech.ru.common_kotlin

@Deprecated("Use Java 8 API `java.util.Optional`")
data class Option<T : Any>(
    val value: T?
) {
    val isEmpty = value == null
    val isNotEmpty = !isEmpty
}