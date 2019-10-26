package com.example.map.di

import kekmech.ru.map.MapFragmentPresenter
import com.example.map.model.MapFragmentModel
import kekmech.ru.map.model.MapFragmentModelImpl
import kekmech.ru.map.view.MapFragment
import kekmech.ru.map.view.MapFragmentView
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope

@Module(subcomponents = [
    MapFragmentComponent::class
])
abstract class MapFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(MapFragment::class)
    abstract fun bindMapFragmentInjectorFactory(factory: MapFragmentComponent.Factory):AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun providePresenter(presenter: MapFragmentPresenter): Presenter<MapFragmentView>

    @ActivityScope
    @Binds
    abstract fun provideModel(model: MapFragmentModelImpl): MapFragmentModel
}