package kekmech.ru.lib_coroutines.di

import kekmech.ru.lib_coroutines.CoroutineDispatchers
import kekmech.ru.lib_coroutines.CoroutineDispatchersImpl
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

public val LibraryCoroutinesModule: Module = module {
    factory { CoroutineDispatchersImpl() } bind CoroutineDispatchers::class
}
