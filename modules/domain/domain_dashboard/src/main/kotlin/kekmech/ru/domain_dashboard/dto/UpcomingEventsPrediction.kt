package kekmech.ru.domain_dashboard.dto

import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction.ClassesInNDays
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction.ClassesTodayNotStarted
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction.ClassesTodayStarted
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction.NoClassesNextWeek
import kekmech.ru.domain_schedule_models.dto.Classes
import java.time.LocalDate
import kotlin.time.Duration

/**
 * Something like an algebraic type to display the status of upcoming events on the dashboard.
 *
 * - [NoClassesNextWeek] - No upcoming events during the week
 * - [ClassesTodayNotStarted] - Nearest events are today's classes, but classes not already started
 * - [ClassesTodayStarted] - Nearest events are today's classes, and some classes have already
 * started or even ended
 * - [ClassesInNDays] - Nearest events will take place during the week
 */
sealed interface UpcomingEventsPrediction {

    object NoClassesNextWeek : UpcomingEventsPrediction

    /**
     * @param timeLeft Time left until the start of the today's classes
     * @param futureClasses Classes that will be started today
     */
    data class ClassesTodayNotStarted(
        val timeLeft: Duration,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction

    /**
     * @param inProgress Classes that have already begun
     * @param futureClasses Next classes after [inProgress] classes
     */
    data class ClassesTodayStarted(
        val inProgress: Classes,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction

    /**
     * @param date Date on which the next classes will take place
     * @param timeLeft Time left until the start of the next classes
     * @param futureClasses Classes that will be started in [timeLeft] time
     */
    data class ClassesInNDays(
        val date: LocalDate,
        val timeLeft: Duration,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction
}
