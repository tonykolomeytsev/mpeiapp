package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm

import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.BACK
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.BACK_WITH_RESULT
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.DASHBOARD
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent.Internal
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleCommand as Command
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEffect as Effect
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent as Event
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleState as State

val GROUP_NUMBER_PATTERN = "[а-яА-Я]+-[а-яА-Я0-9]+-[0-9]+".toRegex()
val PERSON_NAME_PATTERN = "[а-яА-Я]+\\s+([а-яА-Я]+\\s?)+".toRegex()

internal class FindScheduleReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.FindScheduleFailure -> {
                state { copy(isLoading = false) }
                effects { +Effect.ShowLoadingError }
            }

            is Internal.FindScheduleSuccess -> {
                val selectedSchedule = SelectedSchedule(
                    name = event.name,
                    type = event.type,
                )
                state { copy(isLoading = false) }
                when (state.continueTo) {
                    BACK -> effects { +Effect.CloseScreen }
                    BACK_WITH_RESULT -> effects { +Effect.CloseScreenWithResult(selectedSchedule) }
                    DASHBOARD -> commands { +Command.OpenDashboard }
                }
                commands {
                    +Command.SelectSchedule(selectedSchedule)
                        .takeIf { state.selectScheduleAfterSuccess }
                }
            }

            is Internal.SearchForAutocompleteSuccess -> state {
                copy(searchResults = event.results)
            }
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> Unit
            is Ui.Click.Continue -> {
                if (event.scheduleName.isValidGroupNumberOrPersonName()) {
                    state { copy(isLoading = true) }
                    commands { +Command.FindSchedule(name = event.scheduleName) }
                } else {
                    effects { +Effect.ShowInvalidInputError }
                }
            }

            is Ui.Action.GroupNumberChanged -> {
                state {
                    copy(
                        searchResults = state.searchResults.takeIf { event.scheduleName.length > 2 }
                            .orEmpty(),
                    )
                }
                commands {
                    +event.scheduleName.takeIf { it.length >= 2 }
                        ?.let(Command::SearchForAutocomplete)
                }
            }
        }
    }

    private fun String.isValidGroupNumberOrPersonName() =
        isNotBlank() && (matches(GROUP_NUMBER_PATTERN) || matches(PERSON_NAME_PATTERN))
}
