package kekmech.ru.feature_search_impl.screens.main

internal fun String.simplify() = this
    .uppercase()
    .trim()
    .replace("\\s{2,}".toRegex(), " ")
    // Ё and ё are outside of the А-я range, and names may have them (e.g. Алёшин)
    .replace("[^a-zA-Zа-яА-ЯёЁ0-9\\-\\s]".toRegex(), "")
