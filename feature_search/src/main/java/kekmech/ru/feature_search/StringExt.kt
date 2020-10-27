package kekmech.ru.feature_search

internal fun String.simplify() = this
    .toLowerCase()
    .replace("[^a-zA-Zа-яА-Я0-9]".toRegex(), "")