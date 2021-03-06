package kekmech.ru.feature_dashboard

import android.content.Context
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_dashboard.elm.DashboardState
import kekmech.ru.feature_dashboard.items.*
import kekmech.ru.feature_dashboard.upcoming_events.UpcomingEventsListConverter
import java.time.DayOfWeek
import java.time.LocalTime

private const val WEEK_MIN_NUMBER = 0
private const val WEEK_MAX_NUMBER = 17

@Suppress("MagicNumber")
class DashboardListConverter(
    private val context: Context
) {
    private val upcomingEventsListConverter = UpcomingEventsListConverter(context)

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
            val (list, offset) = upcomingEventsListConverter.map(state)
            if (offset == -1) {
                // если нечего показывать в разделе ближайших событий,
                // покажем сначала сессию, потом EmptyStateItem
                addSession(state)

                addAll(list)
                add(SpaceItem.VERTICAL_16)
            } else {
                addAll(list)
                add(SpaceItem.VERTICAL_16)

                // сессию показываем после более близких по времени событий
                addSession(state)
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
            add(SpaceItem.VERTICAL_16)
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

    private fun MutableList<Any>.addSession(state: DashboardState) {
        if (state.sessionScheduleItems.isNullOrEmpty()) return
        add(sessionHeader)
        add(SpaceItem.VERTICAL_12)
        addAll(state.sessionScheduleItems)
        add(SpaceItem.VERTICAL_16)
    }
}
