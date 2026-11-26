package kekmech.ru.feature_bars_impl

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_bars_impl.presentation.compose.formatUpdatedAt
import kekmech.ru.feature_bars_impl.presentation.compose.olderOf
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale
import kotlin.time.Instant

internal class UpdatedAtFormatTest : StringSpec({

    "Current year: day, month and time in the local zone" {
        format(UpdatedAt, Locale.forLanguageTag("ru")) shouldBe "26 ноября, 14:35"
        format(UpdatedAt, Locale.ENGLISH) shouldBe "26 November, 14:35"
    }

    "Another year: the year too" {
        format(UpdatedAt, Locale.forLanguageTag("ru"), today = LocalDate.of(2027, 1, 10)) shouldBe "26 ноября 2026, 14:35"
    }

    "The older of the times, without the missing ones" {
        val older = Instant.parse("2026-11-20T08:00:00Z")
        olderOf(UpdatedAt, older, null) shouldBe older
        olderOf(null, UpdatedAt) shouldBe UpdatedAt
        olderOf(null, null) shouldBe null
    }
})

private val Moscow = ZoneId.of("Europe/Moscow")
private val UpdatedAt = Instant.parse("2026-11-26T11:35:00Z")

private fun format(updatedAt: Instant, locale: Locale, today: LocalDate = LocalDate.of(2026, 11, 26)) =
    formatUpdatedAt(updatedAt, locale, Moscow, today)
