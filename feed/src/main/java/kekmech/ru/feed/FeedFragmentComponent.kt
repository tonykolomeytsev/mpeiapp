package kekmech.ru.feed

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface FeedFragmentComponent : AndroidInjector<FeedFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<FeedFragment>
}