package kekmech.ru.feature_schedule_impl

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_schedule_impl.sync.NightlySyncWindow
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

internal class NightlySyncWindowTest : StringSpec({

    "Evening: wait until 02:00" {
        NightlySyncWindow.delayUntilNextStart(moscow(20, 0)) shouldBe Duration.ofHours(6)
    }

    "After midnight: wait until 02:00 of the same day" {
        NightlySyncWindow.delayUntilNextStart(moscow(1, 30)) shouldBe Duration.ofMinutes(30)
    }

    "Exactly at 02:00 and later: wait until the next night" {
        NightlySyncWindow.delayUntilNextStart(moscow(2, 0)) shouldBe Duration.ofHours(24)
        NightlySyncWindow.delayUntilNextStart(moscow(3, 0)) shouldBe Duration.ofHours(23)
    }

    "Device time zone doesn't matter, the night is in Moscow" {
        // 20:00 in Tbilisi is 19:00 in Moscow
        val tbilisi = ZonedDateTime.of(LocalDateTime.of(2026, 11, 11, 20, 0), ZoneId.of("Asia/Tbilisi"))
        NightlySyncWindow.delayUntilNextStart(tbilisi) shouldBe Duration.ofHours(7)
    }

    "The window is 02:00..06:00 in Moscow, scheduled time with spread stays inside" {
        NightlySyncWindow.contains(moscow(1, 59).toInstant()).shouldBeFalse()
        NightlySyncWindow.contains(moscow(2, 0).toInstant()).shouldBeTrue()
        NightlySyncWindow.contains(moscow(2, 0).plus(NightlySyncWindow.Spread).toInstant()).shouldBeTrue()
        NightlySyncWindow.contains(moscow(6, 0).toInstant()).shouldBeFalse()
        NightlySyncWindow.contains(moscow(14, 0).toInstant()).shouldBeFalse()
    }
}) {

    private companion object {

        fun moscow(hour: Int, minute: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(2026, 11, 11, hour, minute), ZoneId.of("Europe/Moscow"))
    }
}
