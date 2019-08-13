package kekmech.ru.timetable

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.timetable.TimetableFragment

@Subcomponent
interface TimetableFragmentComponent : AndroidInjector<TimetableFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<TimetableFragment>
}