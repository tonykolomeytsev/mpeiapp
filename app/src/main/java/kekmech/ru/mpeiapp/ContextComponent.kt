package kekmech.ru.mpeiapp

import dagger.Component
import kekmech.ru.core.ContextProvider
import javax.inject.Singleton

@Component(modules = [ContextModule::class])
interface ContextComponent: ContextProvider {
    companion object Initializer {
        fun init(contextModule: ContextModule): ContextComponent {
            return DaggerContextComponent.builder()
                .contextModule(contextModule)
                .build()
        }
    }
}