package com.example.map.di

import com.example.map.MapFragmentPresenter
import com.example.map.model.MapFragmentModel
import com.example.map.model.MapFragmentModelImpl
import com.example.map.view.MapFragment
import com.example.map.view.MapFragmentView
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope

@Module(subcomponents = [MapFragmentComponent::class])
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