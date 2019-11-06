package kekmech.ru.bars.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.bars.rights.RightsFragment

@Subcomponent
interface RightsFragmentComponent : AndroidInjector<RightsFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<RightsFragment>
}