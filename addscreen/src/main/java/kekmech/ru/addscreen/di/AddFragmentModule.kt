package kekmech.ru.addscreen.di

import kekmech.ru.addscreen.model.AddFragmentModel
import kekmech.ru.addscreen.model.AddFragmentModelImpl
import kekmech.ru.addscreen.presenter.AddFragmentPresenter
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinAddFragmentModule = module {
    // addfragment MVP pattern
    single { AddFragmentPresenter(get(), get(), get(), get()) }
    single { AddFragmentModelImpl(get(), get(), get()) } bind AddFragmentModel::class
}