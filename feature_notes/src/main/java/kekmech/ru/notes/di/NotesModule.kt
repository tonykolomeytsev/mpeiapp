package kekmech.ru.notes.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_notes.*
import kekmech.ru.notes.NotesFeatureLauncherImpl
import kekmech.ru.notes.all_notes.AllNotesAnalytics
import kekmech.ru.notes.all_notes.mvi.AllNotesActor
import kekmech.ru.notes.all_notes.mvi.AllNotesFeatureFactory
import kekmech.ru.notes.edit.NoteEditAnalytics
import kekmech.ru.notes.edit.mvi.NoteEditActor
import kekmech.ru.notes.edit.mvi.NoteEditFeatureFactory
import kekmech.ru.notes.note_list.NoteListAnalytics
import kekmech.ru.notes.note_list.mvi.NoteListActor
import kekmech.ru.notes.note_list.mvi.NoteListFeatureFactory
import org.koin.dsl.bind

object NotesModule : ModuleProvider({
    single { CachedNotesSource(NotesSourceImpl(get())) } bind NotesSource::class
    single { NotesRepository(get(), get()) } bind NotesRepository::class
    factory { NotesDependencies(get(), get(), get()) } bind NotesDependencies::class
    factory { NotesFeatureLauncherImpl(get()) } bind NotesFeatureLauncher::class
    factory { NotesScheduleTransformer(get()) }

    factory { NoteListFeatureFactory(get()) } bind NoteListFeatureFactory::class
    factory { NoteListActor(get()) } bind NoteListActor::class

    factory { NoteEditFeatureFactory(get()) } bind NoteEditFeatureFactory::class
    factory { NoteEditActor(get()) } bind NoteEditActor::class

    factory { AllNotesFeatureFactory(get()) } bind AllNotesFeatureFactory::class
    factory { AllNotesActor(get()) } bind AllNotesActor::class

    factory { NoteListAnalytics(get()) } bind NoteListAnalytics::class
    factory { NoteEditAnalytics(get()) } bind NoteEditAnalytics::class
    factory { AllNotesAnalytics(get()) } bind AllNotesAnalytics::class
})