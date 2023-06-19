package kekmech.ru.feature_notes.di

import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesStoreFactory
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditStoreFactory
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListStoreFactory

internal data class NotesDependencies(
    val noteListStoreFactory: NoteListStoreFactory,
    val noteEditStoreFactory: NoteEditStoreFactory,
    val allNotesStoreFactory: AllNotesStoreFactory,
    val searchFeatureLauncher: SearchFeatureLauncher
)
