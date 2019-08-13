package kekmech.ru.mainscreen

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.feed.FeedFragmentModule
import kekmech.ru.feed.TimetableFragmentModule

@ActivityScope
@Subcomponent(modules = [FeedFragmentModule::class, TimetableFragmentModule::class])
interface MainActivityComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}