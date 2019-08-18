package kekmech.ru.settings.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.settings.SettingsFragment

@Subcomponent
interface SettingsFragmentComponent : AndroidInjector<SettingsFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<SettingsFragment>
}