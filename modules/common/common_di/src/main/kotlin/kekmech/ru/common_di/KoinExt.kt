package kekmech.ru.common_di

import org.koin.core.KoinApplication
import org.koin.core.module.Module

fun KoinApplication.modules(modules: List<Any>) =
    modules(modules.map { if (it is ModuleProvider) it.provider else it as Module })

fun loadKoinModules(modules: List<ModuleProvider>) {
    org.koin.core.context.loadKoinModules(modules.map { it.provider })
}
