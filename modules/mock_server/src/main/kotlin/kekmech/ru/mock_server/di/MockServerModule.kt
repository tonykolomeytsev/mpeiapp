package kekmech.ru.mock_server.di

import kekmech.ru.common_app_lifecycle.AppLifecycleObserver
import kekmech.ru.common_di.bindIntoList
import kekmech.ru.mock_server.initializer.MockServerInitializer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val MockServerModule = module {
    factoryOf(::MockServerInitializer) bindIntoList AppLifecycleObserver::class
}