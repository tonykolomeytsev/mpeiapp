package kekmech.ru.domain_notes.di

import kekmech.ru.common_app_database_api.MigrationV6V7
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.database.migrations.MigrationV6V7Impl
import kekmech.ru.domain_notes.use_cases.GetActualNotesUseCase
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_notes.use_cases.PutNoteForSelectedScheduleUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val DomainNotesModule: Module = module {
    singleOf(::NotesRepository)
    factoryOf(::GetActualNotesUseCase)
    factoryOf(::GetNotesForSelectedScheduleUseCase)
    factoryOf(::PutNoteForSelectedScheduleUseCase)

    factory { MigrationV6V7Impl() } bind MigrationV6V7::class
}
