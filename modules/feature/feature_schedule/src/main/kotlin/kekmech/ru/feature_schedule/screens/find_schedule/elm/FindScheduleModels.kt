package kekmech.ru.feature_schedule.screens.find_schedule.elm

import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType

internal data class FindScheduleState(
    val continueTo: ScheduleFeatureLauncher.ContinueTo,
    val isLoading: Boolean = false,
    val isContinueButtonEnabled: Boolean = false,
    val selectScheduleAfterSuccess: Boolean,
    val searchResults: List<SearchResult> = emptyList(),
)

internal sealed interface FindScheduleEvent {

    sealed interface Ui : FindScheduleEvent {

        object Init : Ui

        object Click {
            data class Continue(val scheduleName: String) : Ui
        }

        object Action {
            data class GroupNumberChanged(val scheduleName: String) : Ui
        }
    }

    sealed interface Internal : FindScheduleEvent {
        data class FindScheduleSuccess(val name: String, val type: ScheduleType) : Internal
        data class FindScheduleFailure(val throwable: Throwable) : Internal
        data class SearchForAutocompleteSuccess(val results: List<SearchResult>) : Internal
    }
}

internal sealed interface FindScheduleEffect {
    object ShowError : FindScheduleEffect
    object ShowSomethingWentWrongError : FindScheduleEffect
    data class NavigateNextFragment(
        val continueTo: ScheduleFeatureLauncher.ContinueTo,
        val selectedSchedule: SelectedSchedule,
    ) : FindScheduleEffect
}

internal sealed interface FindScheduleCommand {
    data class FindSchedule(val name: String) : FindScheduleCommand
    data class SelectSchedule(val selectedSchedule: SelectedSchedule) : FindScheduleCommand
    data class SearchForAutocomplete(val query: String) : FindScheduleCommand
}
