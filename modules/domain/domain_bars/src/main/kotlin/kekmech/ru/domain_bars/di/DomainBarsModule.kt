package kekmech.ru.domain_bars.di

import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_bars.BarsService
import kekmech.ru.domain_bars.data.BarsConfigDataSource
import kekmech.ru.domain_bars.data.BarsConfigRepository
import kekmech.ru.domain_bars.data.BarsConfigRepositoryImpl
import kekmech.ru.domain_bars.data.BarsExtractJsDataSource
import kekmech.ru.domain_bars.data.BarsExtractJsRepository
import kekmech.ru.domain_bars.data.BarsExtractJsRepositoryImpl
import kekmech.ru.domain_bars.data.BarsUserInfoRepository
import kekmech.ru.domain_bars.data.BarsUserInfoRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val DomainBarsModule = module {
    factoryOf(::BarsConfigDataSource)
    factoryOf(::BarsConfigRepositoryImpl) bind BarsConfigRepository::class
    factoryOf(::BarsExtractJsDataSource)
    factoryOf(::BarsExtractJsRepositoryImpl) bind BarsExtractJsRepository::class
    factoryOf(::BarsUserInfoRepositoryImpl) bind BarsUserInfoRepository::class
    single { get<Retrofit.Builder>().buildApi<BarsService>() } bind BarsService::class
}
