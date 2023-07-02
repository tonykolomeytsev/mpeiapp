package kekmech.ru.domain_notes.di

import kekmech.ru.common_app_database_api.MigrationV6V7
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.database.migrations.MigrationV6V7Impl
import kekmech.ru.domain_notes.interactors.GetActualNotesInteractor
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_notes.use_cases.PutNoteForSelectedScheduleUseCase
import kekmech.ru.ext_koin.bindIntoList
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DomainNotesModule = module {
    singleOf(::NotesRepository)
    factoryOf(::GetActualNotesInteractor)
    factoryOf(::GetNotesForSelectedScheduleUseCase)
    factoryOf(::PutNoteForSelectedScheduleUseCase)

    factory { MigrationV6V7Impl() } bindIntoList MigrationV6V7::class
}
