package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm

import io.reactivex.rxjava3.exceptions.CompositeException
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent.Internal
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent.Ui
import retrofit2.HttpException
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleCommand as Command
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEffect as Effect
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent as Event
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleState as State

private const val HTTP_BAD_REQUEST_CODE = 400
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
                effects { +calculateErrorEffect(event.throwable) }
            }

            is Internal.FindScheduleSuccess -> {
                val selectedSchedule = SelectedSchedule(
                    name = event.name,
                    type = event.type,
                )
                state { copy(isLoading = false) }
                effects {
                    +Effect.NavigateNextFragment(
                        continueTo = state.continueTo,
                        selectedSchedule = selectedSchedule,
                    )
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
                state { copy(isLoading = true) }
                commands { +Command.FindSchedule(name = event.scheduleName) }
            }

            is Ui.Action.GroupNumberChanged -> {
                state {
                    copy(
                        isContinueButtonEnabled = event.scheduleName.isValidGroupNumberOrPersonName(),
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


    private fun calculateErrorEffect(throwable: Throwable): Effect {
        return if (throwable is CompositeException &&
            throwable.exceptions.any { it is HttpException && it.code() == HTTP_BAD_REQUEST_CODE }
        ) {
            Effect.ShowError
        } else {
            Effect.ShowSomethingWentWrongError
        }
    }

    private fun String.isValidGroupNumberOrPersonName() =
        isNotBlank() && (matches(GROUP_NUMBER_PATTERN) || matches(PERSON_NAME_PATTERN))
}
