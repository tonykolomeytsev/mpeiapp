package kekmech.ru.map.di

import kekmech.ru.map.MapViewModel
import kekmech.ru.map.model.MapFragmentModel
import kekmech.ru.map.model.MapFragmentModelImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinMapFragmentModule = module {
    // map MVP pattern
    single { MapViewModel(get(), get()) }
    single { MapFragmentModelImpl(get(), get(), get()) } bind MapFragmentModel::class
}