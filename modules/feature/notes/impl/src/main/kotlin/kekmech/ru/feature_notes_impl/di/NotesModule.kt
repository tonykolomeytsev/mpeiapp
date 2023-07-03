package kekmech.ru.feature_notes_impl.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_notes_api.NotesFeatureLauncher
import kekmech.ru.feature_notes_api.domain.service.AttachNotesToScheduleService
import kekmech.ru.feature_notes_api.domain.usecase.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes_api.domain.usecase.PutNoteForSelectedScheduleUseCase
import kekmech.ru.feature_notes_api.interactors.GetActualNotesInteractor
import kekmech.ru.feature_notes_impl.data.database.migrations.MigrationV6V7Impl
import kekmech.ru.feature_notes_impl.data.repository.NotesRepository
import kekmech.ru.feature_notes_impl.domain.usecase.GetNotesForSelectedScheduleUseCaseImpl
import kekmech.ru.feature_notes_impl.domain.usecase.PutNoteForSelectedScheduleUseCaseImpl
import kekmech.ru.feature_notes_impl.launcher.NotesFeatureLauncherImpl
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesActor
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesStoreFactory
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditActor
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditStoreFactory
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListActor
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListStoreFactory
import kekmech.ru.library_app_database.api.MigrationV6V7
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
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

    singleOf(::NotesRepository)
    factoryOf(::GetActualNotesInteractor)
    factoryOf(
        ::GetNotesForSelectedScheduleUseCaseImpl
    ) bind GetNotesForSelectedScheduleUseCase::class
    factoryOf(::PutNoteForSelectedScheduleUseCaseImpl) bind PutNoteForSelectedScheduleUseCase::class

    factory { MigrationV6V7Impl() } bindIntoList MigrationV6V7::class
}
