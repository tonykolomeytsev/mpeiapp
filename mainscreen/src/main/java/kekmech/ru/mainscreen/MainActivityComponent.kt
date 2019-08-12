package kekmech.ru.mainscreen

import android.content.Context
import dagger.Component
import kekmech.ru.core.ContextProvider
import kekmech.ru.core.MainActivityProvider
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.domain.DaggerInteractorComponent
import kekmech.ru.domain.InteractorComponent

@ActivityScope
@Component(dependencies = [ContextProvider::class])
interface MainActivityComponent : MainActivityProvider {
    fun provideContext(): Context

    class Initializer private constructor() {
        companion object {
            fun init(contextProvider: ContextProvider): MainActivityComponent {
                return DaggerMainActivityComponent.builder()
                    .contextProvider(contextProvider)
                    .build()
            }
        }
    }
}