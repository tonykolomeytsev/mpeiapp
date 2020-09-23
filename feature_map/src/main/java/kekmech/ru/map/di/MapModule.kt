package kekmech.ru.map.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_map.MapService
import org.koin.dsl.bind
import retrofit2.Retrofit

object MapModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<MapService>() } bind MapService::class
})