package kekmech.ru.feature_schedule_impl.sync

import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * When the nightly sync runs: at night in Moscow, when phones are mostly idle, charging and on
 * Wi-Fi. Each device picks a random moment inside [Start] + [Spread], so the backend isn't hit
 * by every phone at once.
 */
internal object NightlySyncWindow {

    private val MoscowZone: ZoneId = ZoneId.of("Europe/Moscow")
    private val Start: LocalTime = LocalTime.of(2, 0)
    private val End: LocalTime = LocalTime.of(6, 0)

    /** Random part of the delay, keeps the scheduled time inside [Start]..[End] */
    val Spread: Duration = Duration.ofHours(3)

    /** Time left until the next [Start], never zero: a sync started right now isn't nightly */
    fun delayUntilNextStart(now: ZonedDateTime): Duration {
        val moscowNow = now.withZoneSameInstant(MoscowZone)
        val todayStart = moscowNow.toLocalDate().atTime(Start).atZone(MoscowZone)
        val nextStart = if (todayStart.isAfter(moscowNow)) todayStart else todayStart.plusDays(1)
        return Duration.between(moscowNow, nextStart)
    }

    fun contains(instant: Instant): Boolean {
        val time = instant.atZone(MoscowZone).toLocalTime()
        return !time.isBefore(Start) && time.isBefore(End)
    }
}
