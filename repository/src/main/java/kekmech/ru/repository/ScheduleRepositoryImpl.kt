package kekmech.ru.repository

import kekmech.ru.core.CacheGateway
import kekmech.ru.core.InternetGateway
import kekmech.ru.core.ScheduleRepository
import kekmech.ru.core.dto.CoupleNative

class ScheduleRepositoryImpl(
    val internetGateway: InternetGateway,
    val cacheGateway: CacheGateway
) : ScheduleRepository {

    override fun get(refresh: Boolean): List<CoupleNative> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}