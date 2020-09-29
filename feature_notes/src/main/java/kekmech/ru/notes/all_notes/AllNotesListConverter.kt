package kekmech.ru.notes.all_notes

import kekmech.ru.notes.all_notes.mvi.AllNotesState

class AllNotesListConverter {

    fun map(state: AllNotesState): List<Any> {

        return when {
            state.notes == null -> emptyList()
            else -> mutableListOf<Any>().apply {
                addAll(state.notes)
            }
        }
    }
}