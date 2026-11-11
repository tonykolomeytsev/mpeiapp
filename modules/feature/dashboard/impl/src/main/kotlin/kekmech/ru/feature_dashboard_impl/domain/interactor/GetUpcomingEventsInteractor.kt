package kekmech.ru.feature_dashboard_impl.domain.interactor

import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.ext_kotlin.moscowLocalTime
import kekmech.ru.feature_dashboard_impl.domain.model.UpcomingEventsPrediction
import kekmech.ru.feature_notes_api.domain.service.AttachNotesToScheduleService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.Week
import kotlinx.coroutines.CancellationException
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class GetUpcomingEventsInteractor(
    private val attachNotesToScheduleService: AttachNotesToScheduleService,
) {

    /**
     * @param schedules the weeks to look for the upcoming classes in, usually this and next week
     */
    suspend operator fun invoke(schedules: List<Schedule>): UpcomingEventsPrediction {
        val currentDate = moscowLocalDate()
        val currentTime = moscowLocalTime()
        val days = mutableListOf<Day>()
        for (schedule in schedules) {
            attachNotesSafely(schedule)
            schedule
                .weeks
                .flatMap(Week::days)
                .let { days.addAll(it) }
        }
        days.sortBy(Day::date)
        days.retainAll { day ->
            if (day.date == currentDate) {
                // keep current day only if it has classes right now or in the future
                day.classes.any { cls -> cls.time.end > currentTime }
            } else {
                // keep all future classes
                day.date > currentDate
            }
        }
        return createPredictionFromDays(
            days = days,
            currentDate = currentDate,
            currentTime = currentTime,
        )
    }

    /** Notes are secondary: predict without them rather than not predict at all */
    @Suppress("TooGenericExceptionCaught")
    private suspend fun attachNotesSafely(schedule: Schedule) {
        try {
            attachNotesToScheduleService.attach(schedule)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun createPredictionFromDays(
        days: List<Day>,
        currentDate: LocalDate,
        currentTime: LocalTime,
    ): UpcomingEventsPrediction {
        if (days.isEmpty()) {
            return UpcomingEventsPrediction.NoClassesNextWeek
        }
        val actualDay = days.first()
        val actualDayIsCurrentDay = actualDay.date == currentDate

        return if (actualDayIsCurrentDay) {
            // today we can have classes in progress or only future classes
            val inProgressClasses = actualDay
                .classes
                .find { cls -> currentTime in cls.time.start..cls.time.end }
            if (inProgressClasses != null) {
                predictClassesTodayStarted(actualDay, currentTime, inProgressClasses)
            } else {
                predictClassesTodayNotStarted(actualDay, currentTime)
            }
        } else {
            predictClassesInNDays(actualDay, currentDate, currentTime)
        }
    }

    private fun predictClassesInNDays(
        actualDay: Day,
        currentDate: LocalDate,
        currentTime: LocalTime,
    ): UpcomingEventsPrediction.ClassesInNDays {
        // in the future days we can have only classes in future
        val firstClassesStartDateTime = actualDay
            .classes
            .first()
            .time
            .start
            .atDate(actualDay.date)
        val timeLeft = currentDate
            .atTime(currentTime)
            .until(firstClassesStartDateTime, ChronoUnit.SECONDS)
            .toDuration(DurationUnit.SECONDS)
        return UpcomingEventsPrediction.ClassesInNDays(
            date = actualDay.date,
            dayOffset = currentDate.until(actualDay.date, ChronoUnit.DAYS).toInt(),
            timeLeft = timeLeft,
            futureClasses = actualDay.classes,
        )
    }

    private fun predictClassesTodayNotStarted(
        actualDay: Day,
        currentTime: LocalTime,
    ): UpcomingEventsPrediction.ClassesTodayNotStarted {
        // we do not have classes in progress, only future classes
        val futureClasses = actualDay
            .classes
            .filter { cls -> cls.time.start > currentTime }
        val timeLeft = currentTime
            .until(futureClasses.first().time.start, ChronoUnit.SECONDS)
            .toDuration(DurationUnit.SECONDS)
        return UpcomingEventsPrediction.ClassesTodayNotStarted(
            timeLeft = timeLeft,
            futureClasses = futureClasses,
        )
    }

    private fun predictClassesTodayStarted(
        actualDay: Day,
        currentTime: LocalTime,
        inProgressClasses: Classes,
    ): UpcomingEventsPrediction.ClassesTodayStarted {
        // we have classes in progress
        val restOfFutureClasses = actualDay
            .classes
            .filter { cls -> cls.time.start > currentTime }
        // calculate progress factor
        val time = inProgressClasses.time
        val classesDuration = time.start.until(time.end, ChronoUnit.SECONDS)
        val elapsedTime = time.start.until(currentTime, ChronoUnit.SECONDS)
        return UpcomingEventsPrediction.ClassesTodayStarted(
            inProgressClasses = inProgressClasses,
            inProgressFactor = (elapsedTime.toFloat() / classesDuration.toFloat())
                .coerceIn(0f..1f),
            futureClasses = restOfFutureClasses,
        )
    }
}
