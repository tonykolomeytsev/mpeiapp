package kekmech.ru.addscreen.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.addscreen.AddFragment

@Subcomponent
interface AddFragmentComponent : AndroidInjector<AddFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<AddFragment>
}