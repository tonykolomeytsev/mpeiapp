package kekmech.ru.notes.di

import kekmech.ru.notes.all_notes.mvi.AllNotesFeatureFactory
import kekmech.ru.notes.edit.mvi.NoteEditFeatureFactory
import kekmech.ru.notes.note_list.mvi.NoteListFeatureFactory

data class NotesDependencies(
    val noteListFeatureFactory: NoteListFeatureFactory,
    val noteEditFeatureFactory: NoteEditFeatureFactory,
    val allNotesFeatureFactory: AllNotesFeatureFactory
)