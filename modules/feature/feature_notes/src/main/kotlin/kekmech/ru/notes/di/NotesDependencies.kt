package kekmech.ru.notes.di

import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.notes.all_notes.elm.AllNotesFeatureFactory
import kekmech.ru.notes.edit.elm.NoteEditFeatureFactory
import kekmech.ru.notes.note_list.elm.NoteListFeatureFactory

internal data class NotesDependencies(
    val noteListFeatureFactory: NoteListFeatureFactory,
    val noteEditFeatureFactory: NoteEditFeatureFactory,
    val allNotesFeatureFactory: AllNotesFeatureFactory,
    val searchFeatureLauncher: SearchFeatureLauncher
)
