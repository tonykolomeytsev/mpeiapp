package kekmech.ru.mpeiapp

import android.app.Application
import dagger.Component
import dagger.android.AndroidInjectionModule
import kekmech.ru.core.InteractorProvider
import kekmech.ru.core.RepositoryProvider
import kekmech.ru.domain.InteractorComponent
import kekmech.ru.mainscreen.MainActivityModule
import kekmech.ru.repository.di.RepositoryComponent
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class
    ],
    dependencies = [
        RepositoryProvider::class,
        InteractorProvider::class
    ])
interface AppComponent {
    fun inject(app: MPEIApp)

    companion object {
        fun init(app: Application, cicerone: Cicerone<Router>): AppComponent {
            val contextProvider = ContextComponent.init(ContextModule(app))
            val appModule = AppModule(app, cicerone)
            return DaggerAppComponent.builder()
                .appModule(appModule)
                .repositoryProvider(RepositoryComponent.Initializer.init(contextProvider))
                .interactorProvider(InteractorComponent.Initializer.init(contextProvider))
                .build()
        }
    }
}