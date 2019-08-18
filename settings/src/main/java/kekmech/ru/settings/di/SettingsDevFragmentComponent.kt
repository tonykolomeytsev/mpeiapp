package kekmech.ru.settings.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.settings.SettingsDevFragment

@Subcomponent
interface SettingsDevFragmentComponent : AndroidInjector<SettingsDevFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<SettingsDevFragment>
}