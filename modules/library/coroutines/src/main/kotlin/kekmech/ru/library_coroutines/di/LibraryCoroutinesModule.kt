package kekmech.ru.library_coroutines.di

import kekmech.ru.library_coroutines.CoroutineDispatchers
import kekmech.ru.library_coroutines.CoroutineDispatchersImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val LibraryCoroutinesModule = module {
    factory { CoroutineDispatchersImpl() } bind CoroutineDispatchers::class
}
