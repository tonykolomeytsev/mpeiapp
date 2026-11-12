package kekmech.ru.feature_dashboard_impl.presentation.screen.main

import android.content.Context
import androidx.compose.ui.unit.dp
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.ext_kotlin.moscowLocalTime
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardListItem.Section
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardListItem.Space
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.upcoming_events.UpcomingEventsListConverter
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import java.time.LocalTime
import kekmech.ru.res_strings.R.string as Strings

@Suppress("MagicNumber")
internal class DashboardListConverter(
    private val context: Context,
) {

    private val upcomingEventsListConverter = UpcomingEventsListConverter(context)

    private val notesHeader = DashboardListItem.SectionHeader(
        section = Section.NOTES,
        title = context.getString(Strings.dashboard_section_header_notes),
        actionText = context.getString(Strings.dashboard_section_header_notes_action),
    )

    private val favoritesHeader = DashboardListItem.SectionHeader(
        section = Section.FAVORITES,
        title = context.getString(Strings.dashboard_section_header_favorites),
        subtitle = context.getString(Strings.dashboard_section_header_favorites_subtitle),
        actionText = context.getString(Strings.dashboard_section_header_favorites_action),
    )

    private val formatter = PrettyDateFormatter(context)

    private val lunchStartTime = LocalTime.of(12, 45) // 12:45
    private val lunchEndTime = LocalTime.of(13, 45) // 13:45

    fun map(state: DashboardState): List<DashboardListItem> =
        mutableListOf<DashboardListItem>().apply {
            add(Space("top", 12.dp))
            add(createDayStatusItem(state))
            add(Space("after_day_status", 24.dp))
            add(DashboardListItem.SearchField)
            add(Space("after_search_field", 16.dp))
            add(
                DashboardListItem.SelectedSchedule(
                    name = state.selectedSchedule.name,
                    type = state.selectedSchedule.type,
                )
            )
            add(Space("after_selected_schedule", 8.dp))

            if (state.isWhatsNewBannerVisible) {
                add(DashboardListItem.WhatsNewBanner)
                add(Space("after_whats_new_banner", 16.dp))
            }

            // Section headers have their own top padding, so spaces before them are smaller
            if (moscowLocalTime() in lunchStartTime..lunchEndTime) {
                add(DashboardListItem.LunchBanner)
                add(Space("after_lunch_banner", 16.dp))
            }

            // ближайшие события
            addAll(upcomingEventsListConverter.map(state))
            add(Space("after_upcoming_events", 8.dp))

            // актуальные заметки
            addActualNotes(state)

            state.favoriteSchedules.value?.let { items ->
                add(Space("before_favorites", 8.dp))
                add(favoritesHeader)
                add(Space("after_favorites_header", 4.dp))
                addAll(items.map {
                    DashboardListItem.FavoriteEntry(
                        favoriteSchedule = it,
                        isSelected = it.name.uppercase() == state.selectedSchedule.name.uppercase(),
                    )
                })
            }

            add(Space("bottom", 24.dp))
        }

    private fun MutableList<DashboardListItem>.addActualNotes(state: DashboardState) {
        state.actualNotes.value?.let { notes ->
            add(notesHeader)
            add(Space("after_notes_header", 4.dp))
            if (notes.isNotEmpty()) {
                addAll(notes.mapIndexed { index, note -> DashboardListItem.NoteEntry(index, note) })
            } else {
                add(
                    DashboardListItem.EmptyState(
                        section = Section.NOTES,
                        title = context.getString(Strings.dashboard_actual_notes_empty_state_title),
                        subtitle = context.getString(Strings.all_notes_empty_state_subtitle),
                    )
                )
            }
            add(Space("after_notes", 16.dp))
        }
    }

    private fun createDayStatusItem(state: DashboardState): DashboardListItem.DayStatus {
        val weekStatus =
            when (val weekOfSemester = state.weekOfSemester.value) {
                is WeekOfSemester.Studying -> context.getString(
                    Strings.dashboard_day_status_semester,
                    weekOfSemester.num,
                )

                is WeekOfSemester.NonStudying -> context.getString(Strings.dashboard_day_status_not_semester)
                null -> null
            }
        return DashboardListItem.DayStatus(
            header = formatter.formatAbsolute(moscowLocalDate()),
            description = weekStatus,
        )
    }
}
