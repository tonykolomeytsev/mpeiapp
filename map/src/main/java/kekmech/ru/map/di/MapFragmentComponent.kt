package com.example.map.di

import kekmech.ru.map.view.MapFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MapFragmentComponent : AndroidInjector<MapFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MapFragment>
}