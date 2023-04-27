package kekmech.ru.feature_schedule.screens.find_schedule.elm

import io.reactivex.rxjava3.exceptions.CompositeException
import kekmech.ru.domain_schedule.GROUP_NUMBER_PATTERN
import kekmech.ru.domain_schedule.PERSON_NAME_PATTERN
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleEvent.Internal
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleEvent.Ui
import retrofit2.HttpException
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleCommand as Command
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleEffect as Effect
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleEvent as Event
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleState as State

private const val HTTP_BAD_REQUEST_CODE = 400

internal class FindScheduleReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal): Any =
        when (event) {
            is Internal.FindGroupFailure -> {
                state { copy(isLoading = false) }
                effects { +calculateErrorEffect(event.throwable) }
            }
            is Internal.FindGroupSuccess -> {
                state { copy(isLoading = false) }
                effects { +Effect.NavigateNextFragment(state.continueTo, event.scheduleName) }
                commands {
                    +Command.SelectGroup(event.scheduleName)
                        .takeIf { state.selectScheduleAfterSuccess }
                }
            }
            is Internal.SearchForAutocompleteSuccess -> state {
                copy(searchResults = event.results)
            }
        }

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> Unit
            is Ui.Click.Continue -> {
                state { copy(isLoading = true) }
                commands { +Command.FindGroup(scheduleName = event.scheduleName) }
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