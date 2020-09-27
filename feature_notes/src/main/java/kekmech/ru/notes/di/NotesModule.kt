package kekmech.ru.notes.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.NotesSource
import kekmech.ru.notes.NotesFeatureLauncherImpl
import kekmech.ru.notes.edit.mvi.NoteEditActor
import kekmech.ru.notes.edit.mvi.NoteEditFeatureFactory
import kekmech.ru.notes.note_list.mvi.NoteListActor
import kekmech.ru.notes.note_list.mvi.NoteListFeatureFactory
import org.koin.dsl.bind

object NotesModule : ModuleProvider({
    factory { NotesSource(get()) } bind NotesSource::class
    factory { NotesRepository(get(), get()) } bind NotesRepository::class
    factory { NotesDependencies(get(), get()) } bind NotesDependencies::class
    factory { NotesFeatureLauncherImpl(get()) } bind NotesFeatureLauncher::class

    factory { NoteListFeatureFactory(get()) } bind NoteListFeatureFactory::class
    factory { NoteListActor(get()) } bind NoteListActor::class

    factory { NoteEditFeatureFactory(get()) } bind NoteEditFeatureFactory::class
    factory { NoteEditActor(get()) } bind NoteEditActor::class
})