package kekmech.ru.domain_notes.di

import kekmech.ru.common_app_database_api.MigrationV6V7
import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.database.migrations.MigrationV6V7Impl
import kekmech.ru.domain_notes.use_cases.GetActualNotesUseCase
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_notes.use_cases.PutNoteForSelectedScheduleUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val DomainNotesModule = module {
    singleOf(::NotesRepository)
    factoryOf(::GetActualNotesUseCase)
    factoryOf(::GetNotesForSelectedScheduleUseCase)
    factoryOf(::PutNoteForSelectedScheduleUseCase)

    val migrationQualifier = named("domain_notes_migration")
    factory(migrationQualifier) { MigrationV6V7Impl() } bind MigrationV6V7::class
}
