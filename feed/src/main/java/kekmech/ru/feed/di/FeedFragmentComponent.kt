package kekmech.ru.feed.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.feed.FeedFragment

@Subcomponent
interface FeedFragmentComponent : AndroidInjector<FeedFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<FeedFragment>
}