package kekmech.ru.settings

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface SettingsFragmentComponent : AndroidInjector<SettingsFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<SettingsFragment>
}