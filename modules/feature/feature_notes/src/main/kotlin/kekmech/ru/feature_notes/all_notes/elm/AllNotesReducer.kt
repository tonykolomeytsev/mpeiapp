package kekmech.ru.feature_notes.all_notes.elm

import kekmech.ru.feature_notes.all_notes.elm.AllNotesEvent.News
import kekmech.ru.feature_notes.all_notes.elm.AllNotesEvent.Wish
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

internal class AllNotesReducer :
    StateReducer<AllNotesEvent, AllNotesState, AllNotesEffect, AllNotesAction> {

    override fun reduce(
        event: AllNotesEvent,
        state: AllNotesState
    ): Result<AllNotesState, AllNotesEffect, AllNotesAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: AllNotesState
    ): Result<AllNotesState, AllNotesEffect, AllNotesAction> = when (event) {
        is News.NotesSuccessfullyLoaded -> Result(
            state = state.copy(notes = event.notes)
        )
        is News.NotesLoadError -> Result(
            state = state,
            effect = AllNotesEffect.ShowError(event.throwable)
        )
    }

    private fun reduceWish(
        event: Wish,
        state: AllNotesState
    ): Result<AllNotesState, AllNotesEffect, AllNotesAction> = when (event) {
        is Wish.Init -> Result(
            state = state,
            command = AllNotesAction.LoadAllNotes
        )
        is Wish.Action.DeleteNote -> Result(
            state = state.copy(notes = state.notes?.filter { it != event.note }),
            command = AllNotesAction.DeleteNote(event.note)
        )
    }
}
