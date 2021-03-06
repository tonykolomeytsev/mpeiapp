package kekmech.ru.feature_dashboard

import android.content.Context
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_schedule.utils.withNotePreview
import kekmech.ru.common_schedule.utils.withProgressPreview
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.feature_dashboard.elm.DashboardState
import kekmech.ru.feature_dashboard.elm.NextClassesCondition.NOT_STARTED
import kekmech.ru.feature_dashboard.elm.NextClassesCondition.STARTED
import kekmech.ru.feature_dashboard.helpers.TimeDeclensionHelper
import kekmech.ru.feature_dashboard.helpers.getActualScheduleDayForView
import kekmech.ru.feature_dashboard.helpers.getNextClassesTimeStatus
import kekmech.ru.feature_dashboard.items.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

private const val WEEK_MIN_NUMBER = 0
private const val WEEK_MAX_NUMBER = 17

@Suppress("MagicNumber")
class DashboardListConverter(
    private val context: Context
) {

    private val notesHeader by fastLazy { SectionHeaderItem(
        itemId = SECTION_NOTES_ACTION,
        titleRes = R.string.dashboard_section_header_notes,
        actionNameRes = R.string.dashboard_section_header_notes_action
    ) }

    private val favoritesHeader by fastLazy { SectionHeaderItem(
        itemId = SECTION_FAVORITES_ACTION,
        titleRes = R.string.dashboard_section_header_favorites,
        subtitle = context.getString(R.string.dashboard_section_header_favorites_subtitle),
        actionNameRes = R.string.dashboard_section_header_favorites_action
    ) }

    private val sessionHeader by fastLazy { SectionHeaderItem(
        titleRes = R.string.dashboard_section_header_session,
        subtitle = context.getString(R.string.dashboard_section_header_session_subtitle)
    ) }

    private val formatter = PrettyDateFormatter(context)

    private val lunchStartTime = LocalTime.of(12, 45) // 12:45
    private val lunchEndTime = LocalTime.of(13, 45) // 13:45

    fun map(state: DashboardState): List<Any> {

        return mutableListOf<Any>().apply {
            add(SpaceItem.VERTICAL_12)
            createDayStatusItem(state)?.let {
                add(it)
                add(SpaceItem.VERTICAL_24)
            }
            add(SearchFieldItem)
            add(SpaceItem.VERTICAL_8)
            add(ScheduleTypeItem(
                selectedScheduleName = state.selectedScheduleName,
                selectedScheduleType = state.selectedScheduleType
            ))
            add(SpaceItem.VERTICAL_8)

            listOfNotNull(
                BannerLunchItem.takeIf { moscowLocalTime() in lunchStartTime..lunchEndTime },
                BannerOpenSourceItem.takeIf { moscowLocalDate().dayOfWeek in DayOfWeek.SATURDAY..DayOfWeek.SUNDAY }
            ).let {
                if (it.isNotEmpty()) {
                    addAll(it)
                    add(SpaceItem.VERTICAL_16)
                }
            }

            // ближайшие события
            if (state.currentWeekSchedule == null || state.nextWeekSchedule == null) {
                add(createEventsHeaderItem())
                add(ShimmerItem(EVENTS_SHIMMER_ITEM_ID))
            } else createClassesEventsItems(state)?.let { (header, classes) ->
                add(header)
                add(SpaceItem.VERTICAL_12)
                val actualDay = state.getActualScheduleDayForView()
                addAll(classes
                    .withProgressPreview(nowDate = actualDay?.date ?: LocalDate.MIN)
                    .withNotePreview()
                    .withCalculatedTimeUntilNextClasses(state, actualDay)
                )

                // сессию показываем после более близких по времени событий
                add(SpaceItem.VERTICAL_16)
                addSession(state)

            } ?: run {
                // если нечего показывать в разделе ближайших событий,
                // покажем сначала сессию, потом EmptyStateItem
                addSession(state)

                add(createEventsHeaderItem(subtitle = context.getString(R.string.dashboard_events_empty_state_title),))
                add(SpaceItem.VERTICAL_12)
                add(EmptyStateItem(
                    titleRes = R.string.dashboard_events_empty_state_title,
                    subtitleRes = R.string.dashboard_events_empty_state_subtitle
                ))
            }

            // актуальные заметки
            addActualNotes(state)

            state.favoriteSchedules?.let {
                add(SpaceItem.VERTICAL_16)
                add(favoritesHeader)
                add(SpaceItem.VERTICAL_12)
                addAll(it)
            }

            add(SpaceItem.VERTICAL_24)
        }
    }

    private fun MutableList<Any>.addActualNotes(state: DashboardState) {
        state.notes?.let {
            add(SpaceItem.VERTICAL_16)
            add(notesHeader)
            add(SpaceItem.VERTICAL_12)
            if (it.isNotEmpty()) {
                addAll(it)
            } else {
                add(
                    EmptyStateItem(
                        titleRes = R.string.dashboard_actual_notes_empty_state_title,
                        subtitleRes = R.string.all_notes_empty_state_subtitle
                    )
                )
            }
        }
    }

    private fun createDayStatusItem(state: DashboardState): DayStatusItem? {
        val weekStatus = state.weekOfSemester?.let { weekOfSemester ->
            if (weekOfSemester in WEEK_MIN_NUMBER..WEEK_MAX_NUMBER) {
                context.getString(R.string.dashboard_day_status_semester, weekOfSemester)
            } else {
                context.getString(R.string.dashboard_day_status_not_semester)
            }
        }
        return DayStatusItem(
            formatter.formatAbsolute(moscowLocalDate()),
            weekStatus.orEmpty()
        )
    }

    private fun createClassesEventsItems(state: DashboardState): Pair<Any, List<Any>>? {
        val nowDate = moscowLocalDate()
        val nowTime = moscowLocalTime()

        val isSunday = nowDate.dayOfWeek == DayOfWeek.SUNDAY
        val isEvening = state.todayClasses?.lastOrNull()?.time?.end?.let { it < nowTime } ?: false
        val hasNoClassesToday = state.todayClasses.isNullOrEmpty()

        val selectedScheduleType = state.selectedScheduleType
        when {
            isSunday || isEvening || hasNoClassesToday -> {
                val headerItem = createEventsHeaderItem(context.getString(R.string.dashboard_events_tomorrow))
                val tomorrowClasses = state.tomorrowClasses
                    ?.onEach { it.scheduleType = selectedScheduleType }
                // если на завтра пар тоже нет, то не возвращаем вообще ничего
                return tomorrowClasses?.let { headerItem to it }
            }
            else -> {
                val headerItem = createEventsHeaderItem(context.getString(R.string.dashboard_events_today))
                val nextTodayClasses = state.todayClasses
                    ?.onEach { it.scheduleType = selectedScheduleType }
                    ?.filter { it.time.end > nowTime } // не берем прошедшие пары
                // если на сегодня пар нет, то не возвращаем ничего
                return nextTodayClasses?.let { headerItem to it }
            }
        }
    }

    private fun createEventsHeaderItem(subtitle: String? = null) = SectionHeaderItem(
        title = context.getString(R.string.dashboard_section_header_events),
        subtitle = subtitle
    )

    @Suppress("NestedBlockDepth")
    private fun List<Any>.withCalculatedTimeUntilNextClasses(
        state: DashboardState,
        actualDay: Day?
    ): List<Any> = mutableListOf<Any>().apply {
        val raw = this@withCalculatedTimeUntilNextClasses
        val indexOfNextClasses = raw
            .indexOfFirst { it is Classes }
            .takeIf { it != -1 }
            ?: return raw

        for (e in raw) {
            val classes = e as? Classes
            if (classes == raw[indexOfNextClasses] && actualDay != null) {
                // add time status
                val (condition, hours, minutes) = getNextClassesTimeStatus(actualDay.date, classes.time)
                val prefix = context.getString(R.string.dashboard_item_time_prediction_prefix)
                when (condition) {
                    NOT_STARTED -> add(TextItem(
                        text = "$prefix ${TimeDeclensionHelper.formatHoursMinutes(context, hours, minutes)}"
                    ))
                    STARTED -> Unit
                    else -> Unit
                }
            }
            add(e)
        }
    }

    private fun MutableList<Any>.addSession(state: DashboardState) {
        if (state.sessionScheduleItems.isNullOrEmpty()) return
        add(sessionHeader)
        add(SpaceItem.VERTICAL_12)
        addAll(state.sessionScheduleItems)
        add(SpaceItem.VERTICAL_16)
    }
}
