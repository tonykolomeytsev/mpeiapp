package kekmech.ru.notes.edit.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.notes.edit.mvi.NoteEditEvent.News
import kekmech.ru.notes.edit.mvi.NoteEditEvent.Wish

class NoteEditReducer : BaseReducer<NoteEditState, NoteEditEvent, NoteEditEffect, NoteEditAction> {

    override fun reduce(
        event: NoteEditEvent,
        state: NoteEditState
    ): Result<NoteEditState, NoteEditEffect, NoteEditAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: NoteEditState
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
        state: NoteEditState
    ): Result<NoteEditState, NoteEditEffect, NoteEditAction> = when (event) {
        is Wish.Init -> Result(state = state)
        is Wish.Click.SaveNote -> {
            val newState = state.copy(
                note = state.note.copy(
                    content = event.content
                )
            )
            Result(
                state = newState,
                action = NoteEditAction.SaveNote(newState.note)
            )
        }
    }
}