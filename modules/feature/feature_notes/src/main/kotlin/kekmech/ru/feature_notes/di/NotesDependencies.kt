package kekmech.ru.feature_notes.di

import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_notes.all_notes.elm.AllNotesFeatureFactory
import kekmech.ru.feature_notes.edit.elm.NoteEditFeatureFactory
import kekmech.ru.feature_notes.note_list.elm.NoteListFeatureFactory

internal data class NotesDependencies(
    val noteListFeatureFactory: NoteListFeatureFactory,
    val noteEditFeatureFactory: NoteEditFeatureFactory,
    val allNotesFeatureFactory: AllNotesFeatureFactory,
    val searchFeatureLauncher: SearchFeatureLauncher
)
