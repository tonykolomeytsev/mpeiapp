package kekmech.ru.feature_notes_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.AllNotesEntry
import kekmech.ru.feature_notes_impl.presentation.screen.edit.NoteEditEntry
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.NoteListEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.bottomSheetEntry

internal class NotesNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        entry<AllNotesScreenKey> { AllNotesEntry(it) }
        entry<NoteEditScreenKey> { NoteEditEntry(it) }
        bottomSheetEntry<NoteListScreenKey>(skipPartiallyExpanded = true, isBare = true) {
            NoteListEntry(it)
        }
    }
}
