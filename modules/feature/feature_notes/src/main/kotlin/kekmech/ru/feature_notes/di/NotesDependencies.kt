package kekmech.ru.feature_notes.di

import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesFeatureFactory
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditFeatureFactory
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListFeatureFactory

internal data class NotesDependencies(
    val noteListFeatureFactory: NoteListFeatureFactory,
    val noteEditFeatureFactory: NoteEditFeatureFactory,
    val allNotesFeatureFactory: AllNotesFeatureFactory,
    val searchFeatureLauncher: SearchFeatureLauncher
)
