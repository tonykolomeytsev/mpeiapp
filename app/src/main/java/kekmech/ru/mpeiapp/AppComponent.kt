package kekmech.ru.mpeiapp

import android.app.Application
import dagger.Component
import dagger.android.AndroidInjectionModule
import kekmech.ru.core.InteractorProvider
import kekmech.ru.core.RepositoryProvider
import kekmech.ru.domain.di.InteractorComponent
import kekmech.ru.mainscreen.di.MainActivityModule
import kekmech.ru.repository.di.RepositoryComponent
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
        fun init(app: Application): AppComponent {
            val contextProvider = ContextComponent.init(ContextModule(app))
            val appModule = AppModule(app)
            val repositoryProvider = RepositoryComponent.Initializer.init(contextProvider)
            return DaggerAppComponent.builder()
                .appModule(appModule)
                .repositoryProvider(RepositoryComponent.Initializer.init(contextProvider))
                .interactorProvider(InteractorComponent.Initializer.init(contextProvider, repositoryProvider))
                .build()
        }
    }
}