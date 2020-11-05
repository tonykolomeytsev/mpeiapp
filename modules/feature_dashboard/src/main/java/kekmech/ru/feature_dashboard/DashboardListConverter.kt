package kekmech.ru.feature_dashboard

import android.content.Context
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_dashboard.items.*
import kekmech.ru.feature_dashboard.presentation.DashboardState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class DashboardListConverter(
    private val context: Context
) {

    private val notesHeader by fastLazy { SectionHeaderItem(
        titleRes = R.string.dashboard_section_header_notes,
        actionNameRes = R.string.dashboard_section_header_notes_action
    ) }

    private val favoritesHeader by fastLazy { SectionHeaderItem(
        titleRes = R.string.dashboard_section_header_favorites,
        subtitle = context.getString(R.string.dashboard_section_header_favorites_subtitle)
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
            add(SpaceItem.VERTICAL_12)

            listOfNotNull(
                BannerLunchItem.takeIf { moscowLocalTime() in lunchStartTime..lunchEndTime },
                BannerOpenSourceItem.takeIf { moscowLocalDate().dayOfWeek in DayOfWeek.SATURDAY..DayOfWeek.SUNDAY }
            ).let {
                if (it.isNotEmpty()) {
                    add(SpaceItem.VERTICAL_16)
                    addAll(it)
                    add(SpaceItem.VERTICAL_16)
                }
            }

            // ближайшие события
            createClassesEventsItems(state)?.let { (header, classes) ->
                add(header)
                add(SpaceItem.VERTICAL_12)
                addAll(classes)
            } ?: run {
                // если нечего показывать в разделе ближайших событий, покажем EmptyStateItem
                add(createEventsHeaderItem(
                    subtitle = context.getString(R.string.dashboard_events_empty_state_title),
                    groupName = state.selectedGroupName)
                )
                add(SpaceItem.VERTICAL_12)
                add(EmptyStateItem(
                    titleRes = R.string.dashboard_events_empty_state_title,
                    subtitleRes = R.string.dashboard_events_empty_state_subtitle
                ))
            }

            // актуальные заметки
            state.notes?.let {
                add(SpaceItem.VERTICAL_16)
                add(notesHeader)
                add(SpaceItem.VERTICAL_12)
                if (it.isNotEmpty()) {
                    addAll(it)
                } else {
                    add(EmptyStateItem(
                        titleRes = R.string.dashboard_actual_notes_empty_state_title,
                        subtitleRes = R.string.all_notes_empty_state_subtitle
                    ))
                }
            }

            state.favoriteSchedules?.let {
                add(SpaceItem.VERTICAL_16)
                add(favoritesHeader)
                add(SpaceItem.VERTICAL_12)
                addAll(it)
            }

            add(SpaceItem.VERTICAL_24)
        }
    }

    private fun createDayStatusItem(state: DashboardState): DayStatusItem? {
        val weekStatus = state.weekOfSemester?.let { weekOfSemester ->
            if (weekOfSemester in 1..16) {
                context.getString(R.string.dashboard_day_status_semester, weekOfSemester)
            } else {
                context.getString(R.string.dashboard_day_status_not_semester)
            }
        }
        return DayStatusItem(
            formatter.formatAbsolute(LocalDate.now()),
            weekStatus.orEmpty()
        )
    }

    private fun createClassesEventsItems(state: DashboardState): Pair<Any, List<Any>>? {
        val nowDate = moscowLocalDate()
        val nowTime = moscowLocalTime()

        val isSunday = nowDate.dayOfWeek == DayOfWeek.SUNDAY
        val isEvening = state.todayClasses?.lastOrNull()?.time?.end?.let { it < nowTime } ?: false
        val hasNoClassesToday = state.todayClasses.isNullOrEmpty()

        when {
            isSunday || isEvening || hasNoClassesToday -> {
                val headerItem = createEventsHeaderItem(context.getString(R.string.dashboard_events_tomorrow), state.selectedGroupName)
                val tomorrowClasses = state.tomorrowClasses
                    ?: return null // если на завтра пар тоже нет, то не возвращаем вообще ничего
                return headerItem to tomorrowClasses
            }
            else -> {
                val headerItem = createEventsHeaderItem(context.getString(R.string.dashboard_events_today), state.selectedGroupName)
                val nextTodayClasses = state.todayClasses
                    ?.filter { it.time.end > nowTime } // не берем прошедшие пары
                    ?: return null // если на сегодня пар нет, то не возвращаем ничего
                return headerItem to nextTodayClasses
            }
        }
    }

    private fun createEventsHeaderItem(subtitle: String, groupName: String) = EventsHeaderItem(
        title = context.getString(R.string.dashboard_section_header_events),
        subtitle = subtitle,
        groupName = groupName
    )
}