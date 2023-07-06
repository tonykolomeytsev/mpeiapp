package kekmech.ru.feature_notes_impl.presentation.screen.edit.elm

import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent.Internal
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent.Ui
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditCommand as Command
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEffect as Effect
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent as Event
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditState as State

internal class NoteEditReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal): Any =
        when (event) {
            is Internal.SaveNoteSuccess -> effects { +Effect.CloseWithSuccess }
            is Internal.SaveNoteFailure -> effects { +Effect.ShowError }
        }

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> Unit
            is Ui.Click.SaveNote -> commands { +Command.SaveNote(state.note) }
            is Ui.Action.NoteContentChanged ->
                state {
                    copy(note = state.note.copy(content = event.content))
                }
        }
}
