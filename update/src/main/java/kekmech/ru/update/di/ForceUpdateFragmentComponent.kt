package kekmech.ru.update.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.update.view.ForceUpdateFragment

@Subcomponent
interface ForceUpdateFragmentComponent : AndroidInjector<ForceUpdateFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<ForceUpdateFragment>
}