package kekmech.ru.settings.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.settings.SettingsDevFragment

@Module(subcomponents = [SettingsDevFragmentComponent::class])
abstract class SettingsDevFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsDevFragment::class)
    abstract fun bindSettingsFragmentInjectorFactory(factory: SettingsDevFragmentComponent.Factory):AndroidInjector.Factory<*>
}