package kekmech.ru.notes.all_notes.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.notes.all_notes.mvi.AllNotesEvent.News
import kekmech.ru.notes.all_notes.mvi.AllNotesEvent.Wish

internal class AllNotesReducer : BaseReducer<AllNotesState, AllNotesEvent, AllNotesEffect, AllNotesAction> {

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
            action = AllNotesAction.LoadAllNotes
        )
        is Wish.Action.DeleteNote -> Result(
            state = state.copy(notes = state.notes?.filter { it != event.note }),
            action = AllNotesAction.DeleteNote(event.note)
        )
    }
}