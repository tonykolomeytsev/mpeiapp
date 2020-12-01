package kekmech.ru.notes.di

import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.notes.all_notes.mvi.AllNotesFeatureFactory
import kekmech.ru.notes.edit.mvi.NoteEditFeatureFactory
import kekmech.ru.notes.note_list.mvi.NoteListFeatureFactory

internal data class NotesDependencies(
    val noteListFeatureFactory: NoteListFeatureFactory,
    val noteEditFeatureFactory: NoteEditFeatureFactory,
    val allNotesFeatureFactory: AllNotesFeatureFactory,
    val searchFeatureLauncher: SearchFeatureLauncher
)