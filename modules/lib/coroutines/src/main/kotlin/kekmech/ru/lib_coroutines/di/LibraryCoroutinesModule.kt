package kekmech.ru.lib_coroutines.di

import kekmech.ru.lib_coroutines.CoroutineDispatchers
import kekmech.ru.lib_coroutines.CoroutineDispatchersImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val LibraryCoroutinesModule = module {
    factory { CoroutineDispatchersImpl() } bind CoroutineDispatchers::class
}
