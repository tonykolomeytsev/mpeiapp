package kekmech.ru.feature_bars_impl

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kekmech.ru.feature_bars_impl.sync.BarsSyncWindow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.random.Random

internal class BarsSyncWindowTest : StringSpec({

    "Daytime and evening: a moment of tomorrow's 08:00..23:00" {
        listOf(moscow(8, 0), moscow(14, 0), moscow(23, 30)).forEach { now ->
            runsOf(now).forEach { run ->
                run.toLocalDate() shouldBe Today.plusDays(1)
                run.shouldBeInWindow()
            }
        }
    }

    "Before 08:00, a run delayed past midnight included: a moment of today's window" {
        listOf(moscow(0, 30), moscow(7, 59)).forEach { now ->
            runsOf(now).forEach { run ->
                run.toLocalDate() shouldBe Today
                run.shouldBeInWindow()
            }
        }
    }

    "Every day is a different moment" {
        runsOf(moscow(12, 0)).distinct().size shouldNotBe 1
    }

    "The whole window is used, the moment isn't stuck near one end" {
        runsOf(moscow(12, 0)).map { it.hour }.toSet() shouldContainOnly (8..22).toSet()
    }

    "Device time zone doesn't matter, the window is in Moscow" {
        // 07:30 in Tbilisi is 06:30 in Moscow: today's window hasn't started there yet
        val tbilisi = ZonedDateTime.of(LocalDateTime.of(Today, LocalTime.of(7, 30)), ZoneId.of("Asia/Tbilisi"))
        runsOf(tbilisi).forEach { run ->
            run.toLocalDate() shouldBe Today
            run.shouldBeInWindow()
        }
    }
}) {

    private companion object {

        val Moscow: ZoneId = ZoneId.of("Europe/Moscow")
        val Today: LocalDate = LocalDate.of(2026, 11, 20)

        fun moscow(hour: Int, minute: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(Today, LocalTime.of(hour, minute)), Moscow)

        /** Planned runs as Moscow local time, for many seeds */
        fun runsOf(now: ZonedDateTime): List<LocalDateTime> =
            (0 until 1000).map { seed ->
                BarsSyncWindow.nextRun(now, Random(seed)).atZone(Moscow).toLocalDateTime()
            }

        fun LocalDateTime.shouldBeInWindow() {
            toLocalTime() shouldBeGreaterThanOrEqualTo LocalTime.of(8, 0)
            toLocalTime() shouldBeLessThan LocalTime.of(23, 0)
        }
    }
}
