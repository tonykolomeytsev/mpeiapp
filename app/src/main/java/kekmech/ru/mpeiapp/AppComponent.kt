package kekmech.ru.mpeiapp

import android.app.Application
import dagger.Component
import dagger.android.AndroidInjectionModule
import kekmech.ru.core.InteractorProvider
import kekmech.ru.core.RepositoryProvider
import kekmech.ru.domain.InteractorComponent
import kekmech.ru.mainscreen.MainActivityModule
import kekmech.ru.repository.RepositoryComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ContextModule::class,
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