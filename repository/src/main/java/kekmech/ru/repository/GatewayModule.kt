package kekmech.ru.repository

import dagger.Binds
import dagger.Module
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.repository.gateways.ScheduleCacheGatewayImpl
import kekmech.ru.repository.gateways.ScheduleRemoteGatewayImpl
import kekmech.ru.repository.gateways.UserCacheGatewayImpl

@Module
abstract class GatewayModule {
    @Binds
    abstract fun provideScheduleCacheGateway(gatewayImpl: ScheduleCacheGatewayImpl): ScheduleCacheGateway

    @Binds
    abstract fun provideScheduleRemoteGateway(gatewayImpl: ScheduleRemoteGatewayImpl): ScheduleRemoteGateway

    @Binds
    abstract fun provideUserCacheGateway(gatewayImpl: UserCacheGatewayImpl): UserCacheGateway
}