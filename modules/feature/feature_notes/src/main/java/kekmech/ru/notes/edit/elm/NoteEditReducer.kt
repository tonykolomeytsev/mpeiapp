package kekmech.ru.notes.edit.elm

import kekmech.ru.notes.edit.elm.NoteEditEvent.News
import kekmech.ru.notes.edit.elm.NoteEditEvent.Wish
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

internal class NoteEditReducer :
    StateReducer<NoteEditEvent, NoteEditState, NoteEditEffect, NoteEditAction> {

    override fun reduce(
        event: NoteEditEvent,
        state: NoteEditState,
    ): Result<NoteEditState, NoteEditEffect, NoteEditAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: NoteEditState,
    ): Result<NoteEditState, NoteEditEffect, NoteEditAction> = when (event) {
        is News.NoteSavedSuccessfully -> Result(
            state = state,
            effect = NoteEditEffect.CloseWithSuccess
        )
        is News.NoteSaveError -> Result(
            state = state,
            effect = NoteEditEffect.ShowError
        )
    }

    private fun reduceWish(
        event: Wish,
        state: NoteEditState,
    ): Result<NoteEditState, NoteEditEffect, NoteEditAction> = when (event) {
        is Wish.Init -> Result(state = state)
        is Wish.Click.SaveNote -> {
            Result(
                state = state,
                command = NoteEditAction.SaveNote(state.note)
            )
        }
        is Wish.Action.NoteContentChanged -> {
            val newState = state.copy(
                note = state.note.copy(content = event.content)
            )
            Result(state = newState)
        }
    }
}
