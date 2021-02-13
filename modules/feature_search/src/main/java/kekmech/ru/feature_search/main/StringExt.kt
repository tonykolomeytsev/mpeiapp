package kekmech.ru.feature_search.main

internal fun String.simplify() = this
    .toLowerCase()
    .trim()
    .replace("\\s{2,}".toRegex(), " ")
    .replace("[^a-zA-Zа-яА-Я0-9\\-\\s]".toRegex(), "")