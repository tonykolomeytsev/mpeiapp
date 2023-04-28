package kekmech.ru.domain_notes.di

import kekmech.ru.domain_notes.NotesRepository
import kekmech.ru.domain_notes.use_cases.GetActualNotesUseCase
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_notes.use_cases.PutNoteForSelectedScheduleUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DomainNotesModule = module {
    singleOf(::NotesRepository)
    factoryOf(::GetActualNotesUseCase)
    factoryOf(::GetNotesForSelectedScheduleUseCase)
    factoryOf(::PutNoteForSelectedScheduleUseCase)
}
