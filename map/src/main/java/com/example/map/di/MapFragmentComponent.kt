package com.example.map.di

import com.example.map.view.MapFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MapFragmentComponent : AndroidInjector<MapFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MapFragment>
}