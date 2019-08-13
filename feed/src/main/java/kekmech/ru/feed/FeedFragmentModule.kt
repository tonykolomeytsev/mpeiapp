package kekmech.ru.feed

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [FeedFragmentComponent::class])
abstract class FeedFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(FeedFragment::class)
    abstract fun bindFeedFragmentInjectorFactory(factory: FeedFragmentComponent.Factory):AndroidInjector.Factory<*>
}