package kekmech.ru.notes.di

import kekmech.ru.notes.NoteFragmentPresenter
import kekmech.ru.notes.model.NoteFragmentModel
import kekmech.ru.notes.model.NoteFragmentModelImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinNoteFragmentModule = module {
    // note fragment MVP
    single { NoteFragmentPresenter(get(), get(), get()) }
    single { NoteFragmentModelImpl(get(), get(), get()) } bind NoteFragmentModel::class
}