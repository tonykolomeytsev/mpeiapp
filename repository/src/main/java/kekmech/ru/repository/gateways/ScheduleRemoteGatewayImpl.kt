package kekmech.ru.repository.gateways

import io.realm.Realm
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import javax.inject.Inject

class ScheduleRemoteGatewayImpl @Inject constructor(realm: Realm) : ScheduleRemoteGateway