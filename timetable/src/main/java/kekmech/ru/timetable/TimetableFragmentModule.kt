package kekmech.ru.feed

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.timetable.TimetableFragment

@Module(subcomponents = [TimetableFragmentComponent::class])
abstract class TimetableFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(TimetableFragment::class)
    abstract fun bindFeedFragmentInjectorFactory(factory: TimetableFragmentComponent.Factory):AndroidInjector.Factory<*>
}