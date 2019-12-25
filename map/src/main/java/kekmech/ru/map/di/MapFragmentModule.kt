package kekmech.ru.map.di

import kekmech.ru.map.MapFragmentPresenter
import kekmech.ru.map.model.MapFragmentModel
import kekmech.ru.map.model.MapFragmentModelImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinMapFragmentModule = module {
    // map MVP pattern
    single { MapFragmentPresenter(get(), get()) }
    single { MapFragmentModelImpl(get(), get(), get(), get(), get()) } bind MapFragmentModel::class
}