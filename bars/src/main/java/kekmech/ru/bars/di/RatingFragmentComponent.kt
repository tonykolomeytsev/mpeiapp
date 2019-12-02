package kekmech.ru.bars.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.bars.details.view.BarsDetailsFragment
import kekmech.ru.bars.rating.view.RatingFragment

@Subcomponent
interface RatingFragmentComponent : AndroidInjector<RatingFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<RatingFragment>
}