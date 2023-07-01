package kekmech.ru.mpeiapp

import kekmech.ru.common_chucker.di.CommonChuckerModule
import kekmech.ru.debug_menu.di.DebugMenuModule
import kekmech.ru.mock_server.di.MockServerModule
import org.koin.core.module.Module

val DebugModules: List<Module> = listOf(
    DebugMenuModule,
    MockServerModule,
    CommonChuckerModule,
)
