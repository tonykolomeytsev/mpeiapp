package kekmech.ru.settings.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.settings.SettingsFragment

@Module(subcomponents = [SettingsFragmentComponent::class])
abstract class SettingsFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsFragment::class)
    abstract fun bindSettingsFragmentInjectorFactory(factory: SettingsFragmentComponent.Factory):AndroidInjector.Factory<*>
}