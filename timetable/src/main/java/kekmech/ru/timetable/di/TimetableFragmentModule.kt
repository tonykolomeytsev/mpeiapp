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
import kekmech.ru.timetable.view.TimetableFragment
import kekmech.ru.timetable.view.TimetableFragmentView
import javax.inject.Singleton

@Module(subcomponents = [TimetableFragmentComponent::class])
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
}