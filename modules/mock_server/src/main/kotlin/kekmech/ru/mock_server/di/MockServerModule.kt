package kekmech.ru.mock_server.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import kekmech.ru.mock_server.initializer.MockServerInitializer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val MockServerModule: Module = module {
    factoryOf(::MockServerInitializer) bindIntoList AppLifecycleObserver::class
}
