package kekmech.ru.feature_dashboard.presentation

import android.content.SharedPreferences
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.common_shared_preferences.boolean
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.feature_dashboard.helpers.getActualScheduleDayForView
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.News
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.Wish
import java.time.DayOfWeek
import java.time.temporal.ChronoUnit

typealias DashboardResult = Result<DashboardState, DashboardEffect, DashboardAction>

class DashboardReducer(
    sharedPreferences: SharedPreferences
) : BaseReducer<DashboardState, DashboardEvent, DashboardEffect, DashboardAction> {

    private var isFeatureBannerEnabled by sharedPreferences.boolean("banner_favorites", true)

    override fun reduce(
        event: DashboardEvent,
        state: DashboardState
    ): DashboardResult = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: DashboardState
    ): DashboardResult = when (event) {
        is News.ScheduleLoaded -> Result(
            state = state.copy(
                currentWeekSchedule = event.schedule.takeIf { event.weekOffset == 0 } ?: state.currentWeekSchedule,
                nextWeekSchedule = event.schedule.takeIf { event.weekOffset == 1 } ?: state.nextWeekSchedule,
                isAfterError = false,
                isLoading = false
            )
        )
        is News.ScheduleLoadError -> Result(
            state = state.copy(
                isAfterError = true,
                isLoading = false
            ),
            effect = DashboardEffect.ShowLoadingError
        )
        is News.SelectedGroupNameLoaded -> Result(
            state = state.copy(selectedScheduleName = event.groupName)
        )
        is News.NotesLoaded -> Result(
            state = state.copy(notes = getImportantNotes(event.notes))
        )
        is News.NotesLoadError -> Result(
            state = state,
            effect = DashboardEffect.ShowNotesLoadingError
        )
        is News.FavoriteSchedulesLoaded -> Result(
            state = state.copy(
                favoriteSchedules = event.favorites
                    .takeIf { it.isNotEmpty() }
                    ?.map { FavoriteScheduleItem(it, it.groupNumber == state.selectedScheduleName) }
            )
        )
        is News.SessionLoaded -> Result(state = state.copy(sessionScheduleItems = event.items))
        is News.FavoriteGroupSelected -> Result(
            state = state,
            effects = emptyList(),
            actions = refreshActions()
        )
    }

    private fun reduceWish(
        event: Wish,
        state: DashboardState
    ): DashboardResult = when (event) {
        is Wish.Init -> Result(
            state = state.copy(
                isLoading = true,
                isFeatureBannerEnabled = isFeatureBannerEnabled
            ),
            effects = emptyList(), // костыль
            actions = refreshActions()
        )
        is Wish.Action.OnSwipeRefresh -> Result(
            state = state.copy(
                isLoading = true
            ),
            effects = emptyList(),
            actions = refreshActions()
        )
        is Wish.Click.OnClasses -> Result(
            state = state,
            effect = state.getActualScheduleDayForView()
                ?.let { day -> DashboardEffect.NavigateToNotesList(event.classes, day.date) }
        )
        is Wish.Action.SilentUpdate -> Result(
            state = state,
            effects = emptyList(),
            actions = refreshActions()
        )
        is Wish.Action.SelectFavoriteSchedule -> {
            val newGroupNumber = event.favoriteSchedule.groupNumber
            Result(
                state = state.copy(selectedScheduleName = newGroupNumber),
                effects = emptyList(),
                actions = listOf(DashboardAction.SelectGroup(newGroupNumber))
            )
        }
        is Wish.Action.CloseFeatureBanner -> {
            isFeatureBannerEnabled = false
            Result(
                state = state.copy(isFeatureBannerEnabled = false)
            )
        }
    }

    private fun refreshActions(): List<DashboardAction> {
        return listOfNotNull(
            DashboardAction.GetSelectedGroupName,
            DashboardAction.LoadNotes,
            DashboardAction.LoadSchedule(0),
            DashboardAction.LoadSchedule(1)
                .takeIf { moscowLocalDate().dayOfWeek == DayOfWeek.SUNDAY },
            DashboardAction.LoadFavoriteSchedules,
            DashboardAction.LoadSession
        )
    }

    @Suppress("MagicNumber")
    private fun getImportantNotes(notes: List<Note>): List<Note> =
        notes
            .filter { // todo make with settings
                ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) in 0..7
            }
            .sortedBy { it.dateTime }
            .take(5) // todo make with settings
}