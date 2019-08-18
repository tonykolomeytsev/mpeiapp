package kekmech.ru.repository

import dagger.Component
import kekmech.ru.core.ContextProvider
import kekmech.ru.core.RepositoryProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class], dependencies = [ContextProvider::class])
interface RepositoryComponent: RepositoryProvider {
    class Initializer private constructor() {
        companion object {
            fun init(contextProvider: ContextProvider): RepositoryComponent {
                return DaggerRepositoryComponent.builder()
                    .contextProvider(contextProvider)
                    .build()
            }
        }
    }
}