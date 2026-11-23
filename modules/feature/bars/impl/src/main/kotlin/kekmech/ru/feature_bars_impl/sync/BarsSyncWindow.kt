package kekmech.ru.feature_bars_impl.sync

import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.random.Random

/**
 * When the daily BARS refresh runs: once a day, at a random moment of the Moscow daytime, when students
 * open the site themselves. Every device picks a new moment every day, so the site sees neither a pattern
 * nor a spike.
 */
internal object BarsSyncWindow {

    private val MoscowZone: ZoneId = ZoneId.of("Europe/Moscow")
    private val Start: LocalTime = LocalTime.of(8, 0)

    /** Until 23:00 */
    private val Length: Duration = Duration.ofHours(15)

    /**
     * A random moment of the next window: today's if it hasn't started yet, otherwise tomorrow's.
     * So a run delayed past midnight by the system doesn't skip the next day.
     */
    fun nextRun(now: ZonedDateTime, random: Random = Random.Default): Instant {
        val moscowNow = now.withZoneSameInstant(MoscowZone)
        val todayStart = moscowNow.toLocalDate().atTime(Start).atZone(MoscowZone)
        val start = if (moscowNow.isBefore(todayStart)) todayStart else todayStart.plusDays(1)
        return start.toInstant().plusSeconds(random.nextLong(Length.seconds))
    }
}
