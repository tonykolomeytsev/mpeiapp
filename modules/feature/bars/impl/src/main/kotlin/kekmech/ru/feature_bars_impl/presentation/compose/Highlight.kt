package kekmech.ru.feature_bars_impl.presentation.compose

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

/**
 * Applies [style] to the first occurrence of [value] in this string, e.g. to highlight a number
 * that was put into a string resource.
 */
internal fun String.highlight(value: String, style: SpanStyle): AnnotatedString {
    val start = indexOf(value)
    return buildAnnotatedString {
        append(this@highlight)
        if (start >= 0) addStyle(style, start, start + value.length)
    }
}
