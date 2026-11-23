package kekmech.ru.feature_bars_impl.presentation.compose

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

/**
 * [part] of [total] as a percent: "3,13 %" for ru and "3.13%" for en. Rounds half up like the BARS site
 * (1 of 32 is "3,13" there), `NumberFormat` alone rounds half even and would give "3,12".
 */
internal fun formatPercent(part: Int, total: Int, locale: Locale): String =
    NumberFormat.getPercentInstance(locale)
        .apply {
            maximumFractionDigits = 2
            roundingMode = RoundingMode.HALF_UP
        }
        .format(if (total > 0) part.toDouble() / total else 0.0)
