package kekmech.ru.domain

import dagger.Component
import kekmech.ru.core.ContextProvider
import kekmech.ru.core.InteractorProvider

@Component(modules = [InteractorModule::class], dependencies = [ContextProvider::class])
interface InteractorComponent: InteractorProvider {
    class Initializer private constructor() {
        companion object {
            fun init(contextProvider: ContextProvider): InteractorComponent {
                return DaggerInteractorComponent.builder()
                    .contextProvider(contextProvider)
                    .build()
            }
        }
    }
}