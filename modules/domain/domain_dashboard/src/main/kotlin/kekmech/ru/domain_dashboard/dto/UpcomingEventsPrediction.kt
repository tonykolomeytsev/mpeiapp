package kekmech.ru.domain_dashboard.dto

import kekmech.ru.domain_schedule_models.dto.Classes

sealed interface UpcomingEventsPrediction {

    object NoClassesNextWeek : UpcomingEventsPrediction

    data class ClassesTodayNotStarted(
        val timePrediction: TimePrediction,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction

    data class ClassesTodayStarted(
        val inProgress: Classes,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction

    data class ClassesInNDays(
        val timePrediction: TimePrediction,
        val futureClasses: List<Classes>,
    ) : UpcomingEventsPrediction
}
