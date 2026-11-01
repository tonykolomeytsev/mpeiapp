package kekmech.ru.ui_kit_notes

import android.util.Patterns
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import java.util.regex.Pattern

private val LinkPatterns: List<Pair<Pattern, (String) -> String>> = listOf(
    Patterns.EMAIL_ADDRESS to { "mailto:$it" },
    Patterns.WEB_URL to { if ("://" in it) it else "https://$it" },
    Patterns.PHONE to { "tel:$it" },
)

/**
 * Makes e-mails, web urls and phone numbers in [text] clickable,
 * like `android:autoLink="email|phone|web"` of the old TextView.
 */
internal fun linkify(text: String): AnnotatedString {
    val links = mutableListOf<Pair<IntRange, String>>()
    for ((pattern, toUrl) in LinkPatterns) {
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            val range = matcher.start() until matcher.end()
            // E-mail contains web url, so the first found link wins
            if (links.none { (other, _) -> range.first <= other.last && other.first <= range.last }) {
                links += range to toUrl(matcher.group())
            }
        }
    }
    if (links.isEmpty()) return AnnotatedString(text)
    val styles = TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
    return buildAnnotatedString {
        append(text)
        links.forEach { (range, url) ->
            addLink(LinkAnnotation.Url(url, styles), range.first, range.last + 1)
        }
    }
}
