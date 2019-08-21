package kekmech.ru.domain

import dagger.Component
import kekmech.ru.core.ContextProvider
import kekmech.ru.core.InteractorProvider
import kekmech.ru.core.RepositoryProvider

@Component(modules = [InteractorModule::class], dependencies = [ContextProvider::class, RepositoryProvider::class])
interface InteractorComponent: InteractorProvider {
    class Initializer private constructor() {
        companion object {
            fun init(contextProvider: ContextProvider, repositoryProvider: RepositoryProvider): InteractorComponent {
                return DaggerInteractorComponent.builder()
                    .contextProvider(contextProvider)
                    .repositoryProvider(repositoryProvider)
                    .build()
            }
        }
    }
}