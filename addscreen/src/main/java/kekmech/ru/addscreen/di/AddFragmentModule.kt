package kekmech.ru.addscreen.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.addscreen.AddFragment
import kekmech.ru.addscreen.IAddFragment
import kekmech.ru.addscreen.presenter.AddFragmentPresenter
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope

@Module(subcomponents = [AddFragmentComponent::class])
abstract class AddFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(AddFragment::class)
    abstract fun bindFeedFragmentInjectorFactory(factory: AddFragmentComponent.Factory):AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun providePresenter(presenter: AddFragmentPresenter): Presenter<IAddFragment>
}