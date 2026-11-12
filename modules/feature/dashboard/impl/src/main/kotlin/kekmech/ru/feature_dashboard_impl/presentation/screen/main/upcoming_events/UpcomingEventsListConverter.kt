package kekmech.ru.feature_dashboard_impl.presentation.screen.main.upcoming_events

import android.content.Context
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_dashboard_impl.domain.model.UpcomingEventsPrediction
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardListItem
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardListItem.Section
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardListItem.Space
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.helpers.TimeDeclensionHelper
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_ui.compose.groupParallelClasses
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.lib_elm.Resource
import kotlin.time.Duration
import kekmech.ru.res_strings.R.string as Strings

internal class UpcomingEventsListConverter(
    private val context: Context,
) {

    private val eventsTitle = context.getString(Strings.dashboard_section_header_events)

    fun map(state: DashboardState): List<DashboardListItem> {
        val list = mutableListOf<DashboardListItem>()
        when (state.upcomingEvents) {
            is Resource.Loading -> list.addShimmerItems()
            is Resource.Error -> list.add(DashboardListItem.EventsError(state.upcomingEvents.error))
            is Resource.Data -> {
                when (val prediction = state.upcomingEvents.value) {
                    is UpcomingEventsPrediction.NoClassesNextWeek -> list.addEmptyStateItems()
                    is UpcomingEventsPrediction.ClassesTodayNotStarted ->
                        list.addClassesTodayNotStarted(state.selectedSchedule.type, prediction)

                    is UpcomingEventsPrediction.ClassesTodayStarted ->
                        list.addClassesTodayStarted(state.selectedSchedule.type, prediction)

                    is UpcomingEventsPrediction.ClassesInNDays ->
                        list.addClassesInNDays(state.selectedSchedule.type, prediction)
                }
            }
        }
        return list
    }

    private fun MutableList<DashboardListItem>.addShimmerItems() {
        add(DashboardListItem.SectionHeader(section = Section.EVENTS, title = eventsTitle))
        add(Space("after_events_header", 4.dp))
        add(DashboardListItem.EventsShimmer)
    }

    private fun MutableList<DashboardListItem>.addEmptyStateItems() {
        add(
            DashboardListItem.SectionHeader(
                section = Section.EVENTS,
                title = eventsTitle,
                subtitle = context.getString(Strings.dashboard_events_empty_state_title),
            )
        )
        add(Space("after_events_header", 4.dp))
        add(
            DashboardListItem.EmptyState(
                section = Section.EVENTS,
                title = context.getString(Strings.dashboard_events_empty_state_title),
                subtitle = context.getString(Strings.dashboard_events_empty_state_subtitle),
            )
        )
    }

    private fun MutableList<DashboardListItem>.addEventsHeader(dayOffset: Int) {
        add(
            DashboardListItem.SectionHeader(
                section = Section.EVENTS,
                title = eventsTitle,
                subtitle = TimeDeclensionHelper.formatTimePrediction(
                    context = context,
                    dayOffset = dayOffset,
                ),
            )
        )
        add(Space("after_events_header", 4.dp))
    }

    private fun MutableList<DashboardListItem>.addClassesTodayNotStarted(
        selectedScheduleType: ScheduleType,
        prediction: UpcomingEventsPrediction.ClassesTodayNotStarted,
    ) {
        addEventsHeader(dayOffset = 0)
        addTimePredictionItem(prediction.timeLeft)
        addClasses(prediction.futureClasses, selectedScheduleType)
    }

    private fun MutableList<DashboardListItem>.addClassesTodayStarted(
        selectedScheduleType: ScheduleType,
        prediction: UpcomingEventsPrediction.ClassesTodayStarted,
    ) {
        addEventsHeader(dayOffset = 0)
        addClasses(
            classes = prediction.inProgressClasses,
            scheduleType = selectedScheduleType,
            progress = prediction.inProgressFactor,
        )
        addClasses(prediction.futureClasses, selectedScheduleType)
    }

    private fun MutableList<DashboardListItem>.addClassesInNDays(
        selectedScheduleType: ScheduleType,
        prediction: UpcomingEventsPrediction.ClassesInNDays,
    ) {
        addEventsHeader(dayOffset = prediction.dayOffset)
        if (prediction.dayOffset < 2) {
            addTimePredictionItem(prediction.timeLeft)
        }
        addClasses(prediction.futureClasses, selectedScheduleType)
    }

    private fun MutableList<DashboardListItem>.addTimePredictionItem(timeLeft: Duration) {
        val hours = timeLeft.inWholeHours

        @Suppress("MagicNumber")
        val minutes = timeLeft.inWholeMinutes % 60
        val prefix = context.getString(Strings.dashboard_item_time_prediction_prefix)
        val formattedHoursMinutes =
            TimeDeclensionHelper.formatHoursMinutes(context, hours, minutes)
        if (formattedHoursMinutes.isNotBlank()) {
            add(DashboardListItem.TimePrediction("$prefix $formattedHoursMinutes"))
            add(Space("after_time_prediction", 12.dp))
        }
    }

    /**
     * Adds the [classes] as cards, parallel classes (e.g. labs of different subgroups)
     * are merged into a single card.
     *
     * @param progress progress of the classes, if they are in progress
     */
    private fun MutableList<DashboardListItem>.addClasses(
        classes: List<Classes>,
        scheduleType: ScheduleType,
        progress: Float? = null,
    ) {
        val firstIndex = count { it is DashboardListItem.ClassesEntry }
        classes.groupParallelClasses().forEachIndexed { i, parallelClasses ->
            add(
                DashboardListItem.ClassesEntry(
                    index = firstIndex + i,
                    // Notes are attached by the classes name and time, so the first is used for clicks
                    classes = parallelClasses.first(),
                    model = parallelClasses.toUiModel(
                        scheduleType = scheduleType,
                        progress = progress,
                    ),
                )
            )
        }
    }
}
