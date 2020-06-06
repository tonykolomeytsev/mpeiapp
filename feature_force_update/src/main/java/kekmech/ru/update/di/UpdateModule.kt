package kekmech.ru.update.di

import kekmech.ru.update.ForceUpdateFragmentPresenter
import kekmech.ru.update.model.ForceUpdateFragmentModel
import kekmech.ru.update.model.ForceUpdateFragmentModelImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinUpdateModule = module {
    // update MVP pattern
    single { ForceUpdateFragmentPresenter(get(), get(), get()) }
    single { ForceUpdateFragmentModelImpl(get()) } bind ForceUpdateFragmentModel::class
}