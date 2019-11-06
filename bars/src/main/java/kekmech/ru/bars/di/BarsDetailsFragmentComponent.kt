package kekmech.ru.bars.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.bars.details.view.BarsDetailsFragment

@Subcomponent
interface BarsDetailsFragmentComponent : AndroidInjector<BarsDetailsFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<BarsDetailsFragment>
}