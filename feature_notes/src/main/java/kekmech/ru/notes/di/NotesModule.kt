package kekmech.ru.notes.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.NotesSource
import org.koin.dsl.bind

object NotesModule : ModuleProvider({
    factory { NotesSource(get()) } bind NotesSource::class
    factory { NotesRepository(get(), get()) } bind NotesRepository::class
})