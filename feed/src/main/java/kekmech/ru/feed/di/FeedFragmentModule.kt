package kekmech.ru.feed.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.feed.FeedFragment
import kekmech.ru.feed.IFeedFragment
import kekmech.ru.feed.model.FeedModel
import kekmech.ru.feed.model.FeedModelImpl
import kekmech.ru.feed.presenter.FeedPresenter

@Module(subcomponents = [FeedFragmentComponent::class])
abstract class FeedFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(FeedFragment::class)
    abstract fun bindFeedFragmentInjectorFactory(factory: FeedFragmentComponent.Factory):AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun providePresenter(presenter: FeedPresenter): Presenter<IFeedFragment>

    @ActivityScope
    @Binds
    abstract fun provideModel(model: FeedModelImpl): FeedModel
}