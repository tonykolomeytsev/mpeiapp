package kekmech.ru.bars.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.bars.main.view.BarsFragment

@Subcomponent
interface BarsFragmentComponent : AndroidInjector<BarsFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<BarsFragment>
}