package kekmech.ru.feature_dashboard.screens.main.upcoming_events

import android.content.Context
import kekmech.ru.common_kotlin.Resource
import kekmech.ru.common_schedule.utils.withNotePreview
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.ErrorStateItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.feature_dashboard.R
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState
import kekmech.ru.feature_dashboard.screens.main.helpers.TimeDeclensionHelper
import kekmech.ru.strings.Strings

internal class UpcomingEventsListConverter(
    private val context: Context,
) {

    fun map(state: DashboardState): List<Any> {
        val list = mutableListOf<Any>()
        when (state.upcomingEvents) {
            is Resource.Loading -> list.addShimmerItems()
            is Resource.Error -> list.add(ErrorStateItem(state.upcomingEvents.error))
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

    private fun MutableList<Any>.addShimmerItems() {
        add(SectionHeaderItem(titleRes = Strings.dashboard_section_header_events))
        add(ShimmerItem(R.layout.item_events_shimmer))
    }

    private fun MutableList<Any>.addEmptyStateItems() {
        addAll(
            listOf(
                SectionHeaderItem(
                    titleRes = Strings.dashboard_section_header_events,
                    subtitleRes = Strings.dashboard_events_empty_state_title,
                ),
                SpaceItem.VERTICAL_12,
                EmptyStateItem(
                    titleRes = Strings.dashboard_events_empty_state_title,
                    subtitleRes = Strings.dashboard_events_empty_state_subtitle,
                ),
            ),
        )
    }

    private fun MutableList<Any>.addClassesTodayNotStarted(
        selectedScheduleType: ScheduleType,
        prediction: UpcomingEventsPrediction.ClassesTodayNotStarted,
    ) {
        addAll(
            listOf(
                SectionHeaderItem(
                    titleRes = Strings.dashboard_section_header_events,
                    subtitle = TimeDeclensionHelper.formatTimePrediction(
                        context = context,
                        dayOffset = 0,
                    ),
                ),
                SpaceItem.VERTICAL_12,
            )
        )
        addAll(prediction.futureClasses.handleClasses(selectedScheduleType))
    }

    private fun MutableList<Any>.addClassesTodayStarted(
        selectedScheduleType: ScheduleType,
        prediction: UpcomingEventsPrediction.ClassesTodayStarted,
    ) {
        addAll(
            listOf(
                SectionHeaderItem(
                    titleRes = Strings.dashboard_section_header_events,
                    subtitle = TimeDeclensionHelper.formatTimePrediction(
                        context = context,
                        dayOffset = 0,
                    ),
                ),
                SpaceItem.VERTICAL_12,
            )
        )
        add(prediction.inProgressClasses.copy(progress = prediction.inProgressFactor))
        addAll(prediction.futureClasses.handleClasses(selectedScheduleType))
    }

    private fun MutableList<Any>.addClassesInNDays(
        selectedScheduleType: ScheduleType,
        prediction: UpcomingEventsPrediction.ClassesInNDays,
    ) {
        addAll(
            listOf(
                SectionHeaderItem(
                    titleRes = Strings.dashboard_section_header_events,
                    subtitle = TimeDeclensionHelper.formatTimePrediction(
                        context = context,
                        dayOffset = prediction.dayOffset,
                    ),
                ),
                SpaceItem.VERTICAL_12,
            )
        )
        addAll(prediction.futureClasses.handleClasses(selectedScheduleType))
    }

    /**
     * Apply all necessary modifiers to the list of pairs
     */
    private fun List<Any>.handleClasses(selectedScheduleType: ScheduleType) = this
        .setScheduleType(selectedScheduleType)
        .withNotePreview()

    /**
     * Prepend time prediction item for classes list
     */
//    @Suppress("NestedBlockDepth")
//    private fun List<Any>.withTimePrediction(
//        currentDate: LocalDate,
//        offset: Int,
//    ): List<Any> = mutableListOf<Any>().apply {
//        val raw = this@withTimePrediction
//        var isPredictionAdded = false
//        if (offset > 2) return raw
//        val indexOfNextClasses = raw
//            .indexOfFirst { it is Classes }
//            .takeIf { it != -1 }
//            ?: return raw
//
//        for (e in raw) {
//            val classes = e as? Classes
//            if (classes == raw[indexOfNextClasses] && !isPredictionAdded) {
//                // add time status
//                val (condition, hours, minutes) = getNextClassesTimeStatus(
//                    currentDate,
//                    classes.time
//                )
//                val prefix = context.getString(Strings.dashboard_item_time_prediction_prefix)
//                when (condition) {
//                    NextClassesCondition.NOT_STARTED -> {
//                        val formattedHoursMinutes =
//                            TimeDeclensionHelper.formatHoursMinutes(context, hours, minutes)
//                        if (formattedHoursMinutes.isNotBlank()) {
//                            add(TextItem("$prefix $formattedHoursMinutes"))
//                        }
//                    }
//                    NextClassesCondition.STARTED -> Unit
//                    else -> Unit
//                }
//                isPredictionAdded = true
//            }
//            add(e)
//        }
//    }

    private fun List<Any>.setScheduleType(scheduleType: ScheduleType) =
        onEach { (it as? Classes)?.scheduleType = scheduleType }
}
