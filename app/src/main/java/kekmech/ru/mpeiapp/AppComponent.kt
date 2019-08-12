package kekmech.ru.mpeiapp

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kekmech.ru.core.ContextProvider
import kekmech.ru.core.InteractorProvider
import kekmech.ru.core.MainActivityProvider
import kekmech.ru.core.RepositoryProvider
import kekmech.ru.domain.InteractorComponent
import kekmech.ru.mainscreen.MainActivity
import kekmech.ru.mainscreen.MainActivityComponent
import kekmech.ru.repository.DaggerRepositoryComponent
import kekmech.ru.repository.RepositoryComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ContextModule::class
    ],
    dependencies = [
        RepositoryProvider::class,
        InteractorProvider::class
    ])
interface AppComponent {

    companion object Initializer {
        fun init(app: Application): AppComponent {
            val contextModule = ContextModule(app)
            val contextProvider = ContextComponent.init(contextModule)

            return DaggerAppComponent.builder()
                .contextModule(contextModule)
                .repositoryProvider(RepositoryComponent.Initializer.init(contextProvider))
                .interactorProvider(InteractorComponent.Initializer.init(contextProvider))
                .build()
        }
    }
}