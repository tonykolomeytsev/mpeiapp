package kekmech.ru.feature_notes_impl.di

import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesStoreFactory
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditStoreFactory
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListStoreFactory
import kekmech.ru.feature_search_api.SearchFeatureLauncher

internal data class NotesDependencies(
    val noteListStoreFactory: NoteListStoreFactory,
    val noteEditStoreFactory: NoteEditStoreFactory,
    val allNotesStoreFactory: AllNotesStoreFactory,
    val searchFeatureLauncher: SearchFeatureLauncher
)
