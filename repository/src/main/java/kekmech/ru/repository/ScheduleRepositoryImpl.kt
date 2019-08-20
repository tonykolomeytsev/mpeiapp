package kekmech.ru.repository

import kekmech.ru.core.ScheduleCacheGateway
import kekmech.ru.core.ScheduleRemoteGateway
import kekmech.ru.core.ScheduleRepository
import kekmech.ru.core.dto.CoupleNative

class ScheduleRepositoryImpl(
    val scheduleRemoteGateway: ScheduleRemoteGateway,
    val scheduleCacheGateway: ScheduleCacheGateway
) : ScheduleRepository {

    override fun get(refresh: Boolean): List<CoupleNative> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}