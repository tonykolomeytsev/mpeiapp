package kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm

import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent.Internal
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesCommand as Command
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEffect as Effect
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent as Event
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesState as State

internal class AllNotesReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.LoadAllNotesSuccess -> state { copy(notes = event.notes) }
            is Internal.LoadAllNotesFailure -> effects { +Effect.ShowError(event.throwable) }
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> commands { +Command.LoadAllNotes }
            is Ui.Action.DeleteNote -> {
                state { copy(notes = state.notes?.filter { it != event.note }) }
                commands { +Command.DeleteNote(event.note) }
            }
        }
    }
}
