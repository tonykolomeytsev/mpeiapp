package kekmech.ru.common_coroutines_impl.di

import kekmech.ru.common_coroutines_api.CoroutineDispatchers
import kekmech.ru.common_coroutines_impl.CoroutineDispatchersImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val CommonCoroutinesModule = module {
    factory { CoroutineDispatchersImpl() } bind CoroutineDispatchers::class
}
