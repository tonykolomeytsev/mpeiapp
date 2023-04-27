package kekmech.ru.feature_search.screens.main

internal fun String.simplify() = this
    .uppercase()
    .trim()
    .replace("\\s{2,}".toRegex(), " ")
    .replace("[^a-zA-Zа-яА-Я0-9\\-\\s]".toRegex(), "")
