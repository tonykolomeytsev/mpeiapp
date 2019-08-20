package kekmech.ru.repository

import dagger.Module
import dagger.Provides
import io.realm.Realm
import kekmech.ru.core.*
import kekmech.ru.repository.gateways.CacheGatewayImpl
import kekmech.ru.repository.gateways.InternetGatewayImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm = Realm.getDefaultInstance()

    @Provides
    fun provideCacheGateway(realm: Realm): CacheGateway = CacheGatewayImpl(realm)

    @Provides
    fun provideInternetGateway(realm: Realm): InternetGateway = InternetGatewayImpl(realm)

    @Provides
    @Singleton
    fun provideUserRepository(internetGateway: InternetGateway, cacheGateway: CacheGateway): UserRepository =
        UserRepositoryImpl(internetGateway, cacheGateway)

    @Provides
    @Singleton
    fun provideScheduleRepository(internetGateway: InternetGateway, cacheGateway: CacheGateway): ScheduleRepository =
        ScheduleRepositoryImpl(internetGateway, cacheGateway)

}