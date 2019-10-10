package kekmech.ru.timetable.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.timetable.view.fragments.*

@Subcomponent
interface MondayFragmentComponent : AndroidInjector<MondayFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MondayFragment>
}

@Subcomponent
interface TuesdayFragmentComponent : AndroidInjector<TuesdayFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<TuesdayFragment>
}

@Subcomponent
interface WednesdayFragmentComponent : AndroidInjector<WednesdayFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<WednesdayFragment>
}

@Subcomponent
interface ThursdayFragmentComponent : AndroidInjector<ThursdayFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<ThursdayFragment>
}

@Subcomponent
interface FridayFragmentComponent : AndroidInjector<FridayFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<FridayFragment>
}

@Subcomponent
interface SaturdayFragmentComponent : AndroidInjector<SaturdayFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<SaturdayFragment>
}