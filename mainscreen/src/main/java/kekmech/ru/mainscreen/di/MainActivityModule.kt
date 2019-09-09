package kekmech.ru.mainscreen.di

import androidx.navigation.NavController
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.core.Router
import kekmech.ru.mainscreen.MainActivity
import kekmech.ru.mainscreen.MainNavRouter
import javax.inject.Singleton

@Module(subcomponents = [MainActivityComponent::class])
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    abstract fun bindAndroidInjectorFactory(factory: MainActivityComponent.Factory): AndroidInjector.Factory<*>

    @Binds
    @Singleton
    abstract fun provideNavRouter(routerImpl: MainNavRouter): Router
}