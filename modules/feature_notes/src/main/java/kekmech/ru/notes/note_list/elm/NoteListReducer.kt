package kekmech.ru.notes.note_list.elm

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.notes.note_list.elm.NoteListEvent.News
import kekmech.ru.notes.note_list.elm.NoteListEvent.Wish
import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Result
import java.time.LocalDate
import java.time.LocalDateTime

internal class NoteListReducer :
    StateReducer<NoteListEvent, NoteListState, NoteListEffect, NoteListAction> {

    override fun reduce(
        event: NoteListEvent,
        state: NoteListState
    ): Result<NoteListState, NoteListEffect, NoteListAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: NoteListState
    ): Result<NoteListState, NoteListEffect, NoteListAction> = when (event) {
        is News.NotesLoaded -> Result(
            state = state.copy(
                notes = event.notes
                    .filter { matchesPredicate(it, state.selectedClasses, state.selectedDate) }
            )
        )
        is News.NotesLoadError -> Result(
            state = state,
            effect = NoteListEffect.ShowNoteLoadError
        )
    }

    private fun reduceWish(
        event: Wish,
        state: NoteListState
    ): Result<NoteListState, NoteListEffect, NoteListAction> = when (event) {
        is Wish.Init -> Result(
            state = state,
            command = NoteListAction.LoadNotesForClasses(state.selectedClasses)
        )
        is Wish.Click.CreateNewNote -> Result(
            state = state,
            effect = NoteListEffect.OpenNoteEdit(
                note = Note(
                    content = "",
                    dateTime = LocalDateTime.of(state.selectedDate, state.selectedClasses.time.start),
                    classesName = state.selectedClasses.name
                )
            )
        )
        is Wish.Click.EditNote -> Result(
            state = state,
            effect = NoteListEffect.OpenNoteEdit(event.note)
        )
        is Wish.Action.DeleteNote -> Result(
            state = state.copy(notes = state.notes.filter { it != event.note }),
            command = NoteListAction.DeleteNote(event.note)
        )
    }

    @Suppress("ReturnCount")
    private fun matchesPredicate(note: Note, classes: Classes, date: LocalDate): Boolean {
        if (note.classesName != classes.name) return false
        if (note.dateTime.toLocalDate() != date) return false
        if (note.dateTime.toLocalTime() != classes.time.start) return false
        return true
    }
}