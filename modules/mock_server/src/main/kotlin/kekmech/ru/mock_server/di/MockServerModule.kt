package kekmech.ru.mock_server.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.library_app_lifecycle.AppLifecycleObserver
import kekmech.ru.mock_server.initializer.MockServerInitializer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val MockServerModule = module {
    factoryOf(::MockServerInitializer) bindIntoList AppLifecycleObserver::class
}
