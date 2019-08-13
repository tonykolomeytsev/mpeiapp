package kekmech.ru.timetable

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [TimetableFragmentComponent::class])
abstract class TimetableFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(TimetableFragment::class)
    abstract fun bindTimetableFragmentInjectorFactory(factory: TimetableFragmentComponent.Factory):AndroidInjector.Factory<*>
}