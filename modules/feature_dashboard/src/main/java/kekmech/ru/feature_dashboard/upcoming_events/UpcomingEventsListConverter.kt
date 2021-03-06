package kekmech.ru.feature_dashboard.upcoming_events

import android.content.Context
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.common_schedule.utils.withNotePreview
import kekmech.ru.common_schedule.utils.withProgressPreview
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.ScheduleType
import kekmech.ru.feature_dashboard.EVENTS_SHIMMER_ITEM_ID
import kekmech.ru.feature_dashboard.R
import kekmech.ru.feature_dashboard.elm.DashboardState
import kekmech.ru.feature_dashboard.elm.NextClassesCondition
import kekmech.ru.feature_dashboard.elm.UpcomingEventsMappingResult
import kekmech.ru.feature_dashboard.helpers.TimeDeclensionHelper
import kekmech.ru.feature_dashboard.helpers.getNextClassesTimeStatus
import java.time.LocalDate

class UpcomingEventsListConverter(
    private val context: Context
) {

    fun map(state: DashboardState): UpcomingEventsMappingResult {
        val nowDate = moscowLocalDate()
        val nowTime = moscowLocalTime()
        return when {
            // никаких данных не загружено, показываем шиммеры
            state.currentWeekSchedule == null && state.nextWeekSchedule == null -> LIST_WITH_SHIMMERS
            // загружена либо текущая, либо следующая неделя
            state.currentWeekSchedule != null && state.nextWeekSchedule == null ||
                    state.currentWeekSchedule == null && state.nextWeekSchedule != null -> {
                val actualDayOffset = state.getOffsetForDayWithActualEvents(nowDate, nowTime)
                if (actualDayOffset == -1) {
                    LIST_WITH_SHIMMERS
                } else {
                    state.getClassesForDayWithOffset(nowDate, nowTime, actualDayOffset)
                        ?.setScheduleType(state.selectedScheduleType)
                        ?.withProgressPreview(nowTime, nowDate)
                        ?.withNotePreview()
                        ?.withCalculatedTimeUntilNextClasses(nowDate)
                        ?.attachSectionHeader(actualDayOffset)
                        ?.let { UpcomingEventsMappingResult(it, actualDayOffset) }
                        ?: LIST_WITH_SHIMMERS
                }
            }
            // загружены обе недели, никаких шиммеров в случае отсутствия актуальных данных
            state.currentWeekSchedule != null && state.nextWeekSchedule != null -> {
                val actualDayOffset = state.getOffsetForDayWithActualEvents(nowDate, nowTime)
                if (actualDayOffset == -1) {
                    LIST_WITH_EMPTY_STATE
                } else {
                    state.getClassesForDayWithOffset(nowDate, nowTime, actualDayOffset)
                        ?.setScheduleType(state.selectedScheduleType)
                        ?.withProgressPreview(nowTime, nowDate)
                        ?.withNotePreview()
                        ?.withCalculatedTimeUntilNextClasses(nowDate)
                        ?.attachSectionHeader(actualDayOffset)
                        ?.let { UpcomingEventsMappingResult(it, actualDayOffset) }
                        ?: LIST_WITH_EMPTY_STATE
                }
            }
            else -> LIST_WITH_SHIMMERS
        }
    }

    private fun List<Any>.attachSectionHeader(actualDayOffset: Int): List<Any> =
        listOf(
            SectionHeaderItem(
                titleRes = R.string.dashboard_section_header_events,
                subtitleRes = R.string.dashboard_events_empty_state_title
            ),
            SpaceItem.VERTICAL_12
        ) + this

    @Suppress("NestedBlockDepth")
    private fun List<Any>.withCalculatedTimeUntilNextClasses(
        currentDate: LocalDate
    ): List<Any> = mutableListOf<Any>().apply {
        val raw = this@withCalculatedTimeUntilNextClasses
        val indexOfNextClasses = raw
            .indexOfFirst { it is Classes }
            .takeIf { it != -1 }
            ?: return raw

        for (e in raw) {
            val classes = e as? Classes
            if (classes == raw[indexOfNextClasses]) {
                // add time status
                val (condition, hours, minutes) = getNextClassesTimeStatus(currentDate, classes.time)
                val prefix = context.getString(R.string.dashboard_item_time_prediction_prefix)
                when (condition) {
                    NextClassesCondition.NOT_STARTED -> if (hours < MAX_TIME_PREDICTION_HOURS) {
                        val formattedHoursMinutes =
                            TimeDeclensionHelper.formatHoursMinutes(context, hours, minutes)
                        add(TextItem( "$prefix $formattedHoursMinutes"))
                    }
                    NextClassesCondition.STARTED -> Unit
                    else -> Unit
                }
            }
            add(e)
        }
    }

    private fun List<Any>.setScheduleType(scheduleType: ScheduleType) =
        onEach { (it as? Classes)?.scheduleType = scheduleType }

    private companion object {
        private const val MAX_TIME_PREDICTION_HOURS = 48L
        private val LIST_WITH_SHIMMERS = listOf(
            SectionHeaderItem(titleRes = R.string.dashboard_section_header_events),
            ShimmerItem(EVENTS_SHIMMER_ITEM_ID)
        ).let { UpcomingEventsMappingResult(it, -1) }
        private val LIST_WITH_EMPTY_STATE = listOf(
            SectionHeaderItem(
                titleRes = R.string.dashboard_section_header_events,
                subtitleRes = R.string.dashboard_events_empty_state_title
            ),
            SpaceItem.VERTICAL_12,
            EmptyStateItem(
                titleRes = R.string.dashboard_events_empty_state_title,
                subtitleRes = R.string.dashboard_events_empty_state_subtitle
            )
        ).let { UpcomingEventsMappingResult(it, -1) }
    }
}