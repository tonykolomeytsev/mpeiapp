package kekmech.ru.bars.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.bars.details.BarsDetailsFragmentPresenter
import kekmech.ru.bars.details.view.BarsDetailsFragment
import kekmech.ru.bars.details.view.BarsDetailsFragmentView
import kekmech.ru.bars.main.BarsFragmentPresenter
import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.main.model.BarsFragmentModelImpl
import kekmech.ru.bars.main.view.BarsFragment
import kekmech.ru.bars.main.view.BarsFragmentView
import kekmech.ru.bars.rights.RightsFragment
import kekmech.ru.bars.rights.RightsFragmentPresenter
import kekmech.ru.bars.rights.RightsFragmentView
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope

@Module(subcomponents = [
    BarsFragmentComponent::class,
    RightsFragmentComponent::class,
    BarsDetailsFragmentComponent::class
])
abstract class BarsModule {
    @Binds
    @IntoMap
    @ClassKey(BarsFragment::class)
    abstract fun bindBarsFragmentInjectorFactory(factory: BarsFragmentComponent.Factory): AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun provideBarsFragmentPresenter(presenter: BarsFragmentPresenter): Presenter<BarsFragmentView>

    @ActivityScope
    @Binds
    abstract fun provideBarsFragmentModel(model: BarsFragmentModelImpl): BarsFragmentModel


    @Binds
    @IntoMap
    @ClassKey(RightsFragment::class)
    abstract fun bindRightsFragmentInjectorFactory(factory: RightsFragmentComponent.Factory): AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun provideRightsFragmentPresenter(presenter: RightsFragmentPresenter): Presenter<RightsFragmentView>



    @Binds
    @IntoMap
    @ClassKey(BarsDetailsFragment::class)
    abstract fun bindBarsDetailsFragmentInjectorFactory(factory: BarsDetailsFragmentComponent.Factory): AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun provideBarsDetailsFragmentPresenter(presenter: BarsDetailsFragmentPresenter): Presenter<BarsDetailsFragmentView>

}