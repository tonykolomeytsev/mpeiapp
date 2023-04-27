package kekmech.ru.feature_dashboard.screens.main.upcoming_events

import android.content.Context
import kekmech.ru.common_kotlin.moscowLocalDate
import kekmech.ru.common_kotlin.moscowLocalTime
import kekmech.ru.common_schedule.utils.withNotePreview
import kekmech.ru.common_schedule.utils.withProgressPreview
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.feature_dashboard.R
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState
import kekmech.ru.feature_dashboard.screens.main.elm.NextClassesCondition
import kekmech.ru.feature_dashboard.screens.main.elm.UpcomingEventsMappingResult
import kekmech.ru.feature_dashboard.screens.main.helpers.TimeDeclensionHelper
import kekmech.ru.feature_dashboard.screens.main.helpers.getNextClassesTimeStatus
import kekmech.ru.strings.Strings
import java.time.LocalDate
import java.time.LocalTime

internal class UpcomingEventsListConverter(
    private val context: Context,
) {

    fun map(state: DashboardState): UpcomingEventsMappingResult {
        val nowDate = moscowLocalDate()
        val nowTime = moscowLocalTime()
        return when {
            // загружены обе недели, никаких шиммеров в случае отсутствия актуальных данных
            state.currentWeekSchedule != null && state.nextWeekSchedule != null -> {
                val actualDayOffset = state.getOffsetForDayWithActualEvents(nowDate, nowTime)
                if (actualDayOffset == -1) {
                    LIST_WITH_EMPTY_STATE
                } else {
                    state.getClassesForDayWithOffset(nowDate, nowTime, actualDayOffset)
                        ?.handleClasses(state.selectedScheduleType, nowDate, nowTime, actualDayOffset)
                        ?: LIST_WITH_EMPTY_STATE
                }
            }
            // никаких данных не загружено, показываем шиммеры
            state.currentWeekSchedule == null && state.nextWeekSchedule == null -> {
                LIST_WITH_SHIMMERS
            }
            // загружена либо текущая, либо следующая неделя
            else -> {
                val actualDayOffset = state.getOffsetForDayWithActualEvents(nowDate, nowTime)
                if (actualDayOffset == -1) {
                    LIST_WITH_SHIMMERS
                } else {
                    state.getClassesForDayWithOffset(nowDate, nowTime, actualDayOffset)
                        ?.handleClasses(state.selectedScheduleType, nowDate, nowTime, actualDayOffset)
                        ?: LIST_WITH_SHIMMERS
                }
            }
        }
    }

    /**
     * Apply all necessary modifiers to the list of pairs
     */
    private fun List<Any>.handleClasses(
        selectedScheduleType: ScheduleType,
        nowDate: LocalDate,
        nowTime: LocalTime,
        offset: Int,
    ) = this
        .setScheduleType(selectedScheduleType)
        .withProgressPreview(nowTime, nowDate.plusDays(offset.toLong()))
        .withNotePreview()
        .withTimePrediction(nowDate.plusDays(offset.toLong()), offset)
        .withSectionHeader(offset)
        .let { UpcomingEventsMappingResult(it, offset) }

    /**
     * Prepend section header item to classes list
     */
    private fun List<Any>.withSectionHeader(actualDayOffset: Int): List<Any> =
        listOf(
            SectionHeaderItem(
                titleRes = Strings.dashboard_section_header_events,
                subtitleRes = Strings.dashboard_events_empty_state_title.takeIf { actualDayOffset == -1 },
                subtitle = TimeDeclensionHelper.formatTimePrediction(context, actualDayOffset)
            ),
            SpaceItem.VERTICAL_12
        ) + this

    /**
     * Prepend time prediction item for classes list
     */
    @Suppress("NestedBlockDepth")
    private fun List<Any>.withTimePrediction(
        currentDate: LocalDate,
        offset: Int,
    ): List<Any> = mutableListOf<Any>().apply {
        val raw = this@withTimePrediction
        var isPredictionAdded = false
        if (offset > 2) return raw
        val indexOfNextClasses = raw
            .indexOfFirst { it is Classes }
            .takeIf { it != -1 }
            ?: return raw

        for (e in raw) {
            val classes = e as? Classes
            if (classes == raw[indexOfNextClasses] && !isPredictionAdded) {
                // add time status
                val (condition, hours, minutes) = getNextClassesTimeStatus(currentDate, classes.time)
                val prefix = context.getString(Strings.dashboard_item_time_prediction_prefix)
                when (condition) {
                    NextClassesCondition.NOT_STARTED -> {
                        val formattedHoursMinutes =
                            TimeDeclensionHelper.formatHoursMinutes(context, hours, minutes)
                        if (formattedHoursMinutes.isNotBlank()) {
                            add(TextItem("$prefix $formattedHoursMinutes"))
                        }
                    }
                    NextClassesCondition.STARTED -> Unit
                    else -> Unit
                }
                isPredictionAdded = true
            }
            add(e)
        }
    }

    private fun List<Any>.setScheduleType(scheduleType: ScheduleType) =
        onEach { (it as? Classes)?.scheduleType = scheduleType }

    private companion object {
        private val LIST_WITH_SHIMMERS = listOf(
            SectionHeaderItem(titleRes = Strings.dashboard_section_header_events),
            ShimmerItem(R.layout.item_events_shimmer)
        ).let { UpcomingEventsMappingResult(it, -1) }
        private val LIST_WITH_EMPTY_STATE = listOf(
            SectionHeaderItem(
                titleRes = Strings.dashboard_section_header_events,
                subtitleRes = Strings.dashboard_events_empty_state_title
            ),
            SpaceItem.VERTICAL_12,
            EmptyStateItem(
                titleRes = Strings.dashboard_events_empty_state_title,
                subtitleRes = Strings.dashboard_events_empty_state_subtitle
            )
        ).let { UpcomingEventsMappingResult(it, -1) }
    }
}
