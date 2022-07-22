package kekmech.ru.common_kotlin

fun String.capitalizeSafe(): String =
    replaceFirstChar { it.uppercase() }
