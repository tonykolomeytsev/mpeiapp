package kekmech.ru.feature_bars_impl.presentation

import io.github.tonykolomeytsev.mpeix.sdk.Account

private val WHITESPACE = "\\s+".toRegex()

/**
 * "Иванов Иван Иванович" → "Иван Иванов". BARS writes names as last, first, middle;
 * the app shows first and last only. A single word is returned as is.
 */
internal fun firstLastName(fullName: String): String {
    val parts = fullName.trim().split(WHITESPACE)
    return if (parts.size >= 2) "${parts[1]} ${parts[0]}" else fullName.trim()
}

/** First and last name of the account, or its username for unfinished accounts. */
internal val Account.shortName: String
    get() = displayName?.takeIf { it.isNotBlank() }?.let(::firstLastName) ?: username
