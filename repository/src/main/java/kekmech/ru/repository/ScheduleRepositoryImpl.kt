package kekmech.ru.repository

import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.dto.CoupleNative

class ScheduleRepositoryImpl(
    val scheduleRemoteGateway: ScheduleRemoteGateway,
    val scheduleCacheGateway: ScheduleCacheGateway
) : ScheduleRepository {

    override fun get(refresh: Boolean): List<CoupleNative> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}