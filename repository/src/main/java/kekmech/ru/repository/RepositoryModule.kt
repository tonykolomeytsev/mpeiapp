package kekmech.ru.repository

import dagger.Module
import dagger.Provides
import io.realm.Realm
import kekmech.ru.core.*
import kekmech.ru.repository.gateways.ScheduleCacheGatewayImpl
import kekmech.ru.repository.gateways.ScheduleRemoteGatewayImpl
import kekmech.ru.repository.gateways.UserCacheGatewayImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm = Realm.getDefaultInstance()

    @Provides
    fun provideScheduleCacheGateway(realm: Realm): ScheduleCacheGateway = ScheduleCacheGatewayImpl(realm)

    @Provides
    fun provideScheduleRemoteGateway(realm: Realm): ScheduleRemoteGateway = ScheduleRemoteGatewayImpl(realm)

    @Provides
    fun provideUserCacheGateway(realm: Realm): UserCacheGateway = UserCacheGatewayImpl(realm)

    @Provides
    @Singleton
    fun provideScheduleRepository(
        scheduleRemoteGateway: ScheduleRemoteGateway,
        scheduleCacheGateway: ScheduleCacheGateway
    ): ScheduleRepository = ScheduleRepositoryImpl(scheduleRemoteGateway, scheduleCacheGateway)

    @Provides
    @Singleton
    fun provideUserRepository(
        userCacheGateway: UserCacheGateway
    ): UserRepository = UserRepositoryImpl(userCacheGateway)

}