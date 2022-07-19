package kekmech.ru.notes.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.NotesScheduleTransformer
import kekmech.ru.domain_notes.sources.CachedNotesSource
import kekmech.ru.domain_notes.sources.NotesSource
import kekmech.ru.domain_notes.sources.NotesSourceImpl
import kekmech.ru.notes.NotesFeatureLauncherImpl
import kekmech.ru.notes.all_notes.elm.AllNotesActor
import kekmech.ru.notes.all_notes.elm.AllNotesFeatureFactory
import kekmech.ru.notes.edit.elm.NoteEditActor
import kekmech.ru.notes.edit.elm.NoteEditFeatureFactory
import kekmech.ru.notes.note_list.elm.NoteListActor
import kekmech.ru.notes.note_list.elm.NoteListFeatureFactory
import org.koin.dsl.bind

object NotesModule : ModuleProvider({
    single { CachedNotesSource(NotesSourceImpl(get())) } bind NotesSource::class
    single { NotesRepository(get(), get()) } bind NotesRepository::class
    factory { NotesDependencies(get(), get(), get(), get()) } bind NotesDependencies::class
    factory { NotesFeatureLauncherImpl(get()) } bind NotesFeatureLauncher::class
    factory { NotesScheduleTransformer(get()) }

    factory { NoteListFeatureFactory(get()) } bind NoteListFeatureFactory::class
    factory { NoteListActor(get()) } bind NoteListActor::class

    factory { NoteEditFeatureFactory(get()) } bind NoteEditFeatureFactory::class
    factory { NoteEditActor(get()) } bind NoteEditActor::class

    factory { AllNotesFeatureFactory(get()) } bind AllNotesFeatureFactory::class
    factory { AllNotesActor(get()) } bind AllNotesActor::class
})