package kekmech.ru.feature_dashboard.screens.main

import android.content.Context
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_kotlin.moscowLocalDate
import kekmech.ru.common_kotlin.moscowLocalTime
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_schedule_models.dto.WeekOfSemester
import kekmech.ru.feature_dashboard.items.BannerLunchItem
import kekmech.ru.feature_dashboard.items.BannerOpenSourceItem
import kekmech.ru.feature_dashboard.items.DayStatusItem
import kekmech.ru.feature_dashboard.items.ScheduleTypeItem
import kekmech.ru.feature_dashboard.items.SearchFieldItem
import kekmech.ru.feature_dashboard.screens.main.DashboardFragment.Companion.SECTION_FAVORITES_ACTION
import kekmech.ru.feature_dashboard.screens.main.DashboardFragment.Companion.SECTION_NOTES_ACTION
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState
import kekmech.ru.feature_dashboard.screens.main.upcoming_events.UpcomingEventsListConverter
import kekmech.ru.strings.Strings
import java.time.DayOfWeek
import java.time.LocalTime

@Suppress("MagicNumber")
internal class DashboardListConverter(
    private val context: Context,
) {

    private val upcomingEventsListConverter = UpcomingEventsListConverter(context)

    private val notesHeader by fastLazy {
        SectionHeaderItem(
            itemId = SECTION_NOTES_ACTION,
            titleRes = Strings.dashboard_section_header_notes,
            actionNameRes = Strings.dashboard_section_header_notes_action
        )
    }

    private val favoritesHeader by fastLazy {
        SectionHeaderItem(
            itemId = SECTION_FAVORITES_ACTION,
            titleRes = Strings.dashboard_section_header_favorites,
            subtitle = context.getString(Strings.dashboard_section_header_favorites_subtitle),
            actionNameRes = Strings.dashboard_section_header_favorites_action
        )
    }

    private val formatter = PrettyDateFormatter(context)

    private val lunchStartTime = LocalTime.of(12, 45) // 12:45
    private val lunchEndTime = LocalTime.of(13, 45) // 13:45

    fun map(state: DashboardState): List<Any> {

        return mutableListOf<Any>().apply {
            add(SpaceItem.VERTICAL_12)
            createDayStatusItem(state).let {
                add(it)
                add(SpaceItem.VERTICAL_24)
            }
            add(SearchFieldItem)
            add(SpaceItem.VERTICAL_8)

            state.selectedSchedule.let {
                add(
                    ScheduleTypeItem(
                        selectedScheduleName = it.name,
                        selectedScheduleType = it.type,
                    )
                )
                add(SpaceItem.VERTICAL_8)
            }

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
            val upcomingEvents = upcomingEventsListConverter.map(state)
            addAll(upcomingEvents)
            add(SpaceItem.VERTICAL_16)

            // актуальные заметки
            addActualNotes(state)

            state.favoriteSchedules.value?.let { items ->
                add(SpaceItem.VERTICAL_16)
                add(favoritesHeader)
                add(SpaceItem.VERTICAL_12)
                addAll(items.map {
                    FavoriteScheduleItem(
                        value = it,
                        isSelected = it.name.uppercase() == state.selectedSchedule.name.uppercase()
                    )
                })
            }

            add(SpaceItem.VERTICAL_24)
        }
    }

    private fun MutableList<Any>.addActualNotes(state: DashboardState) {
        state.actualNotes.value?.let {
            add(notesHeader)
            add(SpaceItem.VERTICAL_12)
            if (it.isNotEmpty()) {
                addAll(it)
            } else {
                add(
                    EmptyStateItem(
                        titleRes = Strings.dashboard_actual_notes_empty_state_title,
                        subtitleRes = Strings.all_notes_empty_state_subtitle
                    )
                )
            }
            add(SpaceItem.VERTICAL_16)
        }
    }

    private fun createDayStatusItem(state: DashboardState): DayStatusItem {
        val weekStatus =
            when (val weekOfSemester = state.weekOfSemester.value) {
                is WeekOfSemester.Studying -> context.getString(
                    Strings.dashboard_day_status_semester,
                    weekOfSemester.num,
                )
                is WeekOfSemester.NonStudying -> context.getString(Strings.dashboard_day_status_not_semester)
                null -> null
            }
        return DayStatusItem(
            formatter.formatAbsolute(moscowLocalDate()),
            weekStatus.orEmpty()
        )
    }
}
