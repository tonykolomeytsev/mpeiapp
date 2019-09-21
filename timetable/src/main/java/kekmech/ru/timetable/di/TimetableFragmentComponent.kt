package kekmech.ru.timetable.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.timetable.view.TimetableFragment

@Subcomponent
interface TimetableFragmentComponent : AndroidInjector<TimetableFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<TimetableFragment>
}