package kekmech.ru.feature_notes.di

import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_notes.services.AttachNotesToScheduleService
import kekmech.ru.feature_notes.launcher.NotesFeatureLauncherImpl
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesActor
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesStoreFactory
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditActor
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditStoreFactory
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListActor
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListStoreFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureNotesModule = module {
    factoryOf(::NotesDependencies)
    factoryOf(::NotesFeatureLauncherImpl) bind NotesFeatureLauncher::class
    factoryOf(::AttachNotesToScheduleService)

    factoryOf(::NoteListStoreFactory)
    factoryOf(::NoteListActor)

    factoryOf(::NoteEditStoreFactory)
    factoryOf(::NoteEditActor)

    factoryOf(::AllNotesStoreFactory)
    factoryOf(::AllNotesActor)
}
