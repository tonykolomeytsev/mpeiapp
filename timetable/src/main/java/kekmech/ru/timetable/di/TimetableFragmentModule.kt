package kekmech.ru.timetable.di

import kekmech.ru.timetable.TimetableFragmentPresenter
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.model.TimetableFragmentModelImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinTimetableFragmentModule = module {
    // timetable MVP pattern
    single { TimetableFragmentPresenter(get(), get(), get(), get()) }
    single { TimetableFragmentModelImpl(get(), get(), get(), get(), get(), get(), get(), get()) } bind TimetableFragmentModel::class
}