package kekmech.ru.repository.gateways

import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Inject

class ScheduleRemoteGatewayImpl @Inject constructor(appdb: AppDatabase) : ScheduleRemoteGateway {
    override fun get(dayNum: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}