package kekmech.ru.feature_dashboard_impl.domain.model

import kekmech.ru.feature_schedule_api.domain.model.Classes
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
     * @param inProgressClasses Classes that have already begun
     * @param inProgressFactor Progress value between `0.0` and `1.0` (0% - 100%)
     * @param futureClasses Next classes after [inProgressClasses] classes
     */
    data class ClassesTodayStarted(
        val inProgressClasses: Classes,
        val inProgressFactor: Float,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction

    /**
     * @param date Date on which the next classes will take place
     * @param dayOffset The number of days in which classes will be
     * @param timeLeft Time left until the start of the next classes
     * @param futureClasses Classes that will be started in [timeLeft] time
     */
    data class ClassesInNDays(
        val date: LocalDate,
        val dayOffset: Int,
        val timeLeft: Duration,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction
}
