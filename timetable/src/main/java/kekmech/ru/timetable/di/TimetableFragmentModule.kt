package kekmech.ru.timetable.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.timetable.TimetableFragmentPresenter
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.model.TimetableFragmentModelImpl
import kekmech.ru.timetable.view.DayFragment
import kekmech.ru.timetable.view.TimetableFragment
import kekmech.ru.timetable.view.TimetableFragmentView
import kekmech.ru.timetable.view.fragments.*
import javax.inject.Singleton

@Module(subcomponents = [
    TimetableFragmentComponent::class,
    MondayFragmentComponent::class,
    TuesdayFragmentComponent::class,
    WednesdayFragmentComponent::class,
    ThursdayFragmentComponent::class,
    FridayFragmentComponent::class,
    SaturdayFragmentComponent::class
])
abstract class TimetableFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(TimetableFragment::class)
    abstract fun bindTimetableFragmentInjectorFactory(factory: TimetableFragmentComponent.Factory):AndroidInjector.Factory<*>

    @Binds
    @ActivityScope
    abstract fun providePresenter(presenter: TimetableFragmentPresenter): Presenter<TimetableFragmentView>

    @Binds
    @ActivityScope
    abstract fun provideModel(modelImpl: TimetableFragmentModelImpl): TimetableFragmentModel

    @Binds
    @IntoMap
    @ClassKey(MondayFragment::class)
    abstract fun bindDay1FragmentInjectorFactory(factory: MondayFragmentComponent.Factory):AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ClassKey(TuesdayFragment::class)
    abstract fun bindDay2FragmentInjectorFactory(factory: TuesdayFragmentComponent.Factory):AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ClassKey(WednesdayFragment::class)
    abstract fun bindDay3FragmentInjectorFactory(factory: WednesdayFragmentComponent.Factory):AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ClassKey(ThursdayFragment::class)
    abstract fun bindDay4FragmentInjectorFactory(factory: ThursdayFragmentComponent.Factory):AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ClassKey(FridayFragment::class)
    abstract fun bindDay5FragmentInjectorFactory(factory: FridayFragmentComponent.Factory):AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ClassKey(SaturdayFragment::class)
    abstract fun bindDay6FragmentInjectorFactory(factory: SaturdayFragmentComponent.Factory):AndroidInjector.Factory<*>
}