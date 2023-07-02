package kekmech.ru.domain_map.di

import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_map.MapService
import kekmech.ru.library_network.buildApi
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val DomainMapModule = module {
    single { get<Retrofit.Builder>().buildApi<MapService>() } bind MapService::class
    factoryOf(::MapRepository)
}
