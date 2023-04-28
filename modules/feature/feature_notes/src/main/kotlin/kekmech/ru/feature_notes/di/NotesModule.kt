package kekmech.ru.feature_notes.di

import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.services.AttachNotesToScheduleService
import kekmech.ru.domain_notes.sources.CachedNotesSource
import kekmech.ru.domain_notes.sources.NotesSource
import kekmech.ru.domain_notes.sources.NotesSourceImpl
import kekmech.ru.feature_notes.launcher.NotesFeatureLauncherImpl
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesActor
import kekmech.ru.feature_notes.screens.all_notes.elm.AllNotesFeatureFactory
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditActor
import kekmech.ru.feature_notes.screens.edit.elm.NoteEditFeatureFactory
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListActor
import kekmech.ru.feature_notes.screens.note_list.elm.NoteListFeatureFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureNotesModule = module {
    single { CachedNotesSource(NotesSourceImpl(get())) } bind NotesSource::class
    singleOf(::NotesRepository)
    factoryOf(::NotesDependencies)
    factoryOf(::NotesFeatureLauncherImpl) bind NotesFeatureLauncher::class
    factoryOf(::AttachNotesToScheduleService)

    factoryOf(::NoteListFeatureFactory)
    factoryOf(::NoteListActor)

    factoryOf(::NoteEditFeatureFactory)
    factoryOf(::NoteEditActor)

    factoryOf(::AllNotesFeatureFactory)
    factoryOf(::AllNotesActor)
}
