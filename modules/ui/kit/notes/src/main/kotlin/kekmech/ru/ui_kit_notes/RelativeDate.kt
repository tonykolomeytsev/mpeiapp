package kekmech.ru.ui_kit_notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kekmech.ru.res_strings.R.array as StringArrays
import kekmech.ru.res_strings.R.string as Strings

/**
 * Formats [date] relative to today, e.g. "Tomorrow" or "Tuesday, 22 September".
 * Compose version of `PrettyDateFormatter.formatRelative` from `coreui`.
 */
@Suppress("MagicNumber")
@Composable
@ReadOnlyComposable
public fun relativeDateString(date: LocalDate): String =
    when (ChronoUnit.DAYS.between(LocalDate.now(), date)) {
        0L -> stringResource(Strings.today)
        1L -> stringResource(Strings.tomorrow)
        2L -> stringResource(Strings.after_tomorrow)
        -1L -> stringResource(Strings.yesterday)
        -2L -> stringResource(Strings.before_yesterday)
        else -> {
            val dayOfWeek = stringArrayResource(StringArrays.days_of_week)
                .getOrNull(date.dayOfWeek.value - 1)
                .orEmpty()
            val month = stringArrayResource(StringArrays.months)
                .getOrNull(date.monthValue - 1)
                .orEmpty()
            "$dayOfWeek, ${date.dayOfMonth} $month"
        }
    }
