package kekmech.ru.update.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.update.*
import kekmech.ru.update.model.ForceUpdateFragmentModel
import kekmech.ru.update.model.ForceUpdateFragmentModelImpl
import kekmech.ru.update.view.ForceUpdateFragment
import kekmech.ru.update.view.ForceUpdateFragmentView

@Module(subcomponents = [ForceUpdateFragmentComponent::class])
abstract class UpdateModule {
    @Binds
    @IntoMap
    @ClassKey(ForceUpdateFragment::class)
    abstract fun bindFragmentInjectorFactory(factory: ForceUpdateFragmentComponent.Factory): AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun providePresenter(presenter: ForceUpdateFragmentPresenter): Presenter<ForceUpdateFragmentView>

    @ActivityScope
    @Binds
    abstract fun provideModel(model: ForceUpdateFragmentModelImpl): ForceUpdateFragmentModel
}