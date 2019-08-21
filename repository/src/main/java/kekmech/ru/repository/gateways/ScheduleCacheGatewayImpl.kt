package kekmech.ru.repository.gateways

import io.realm.Realm
import kekmech.ru.core.gateways.ScheduleCacheGateway
import javax.inject.Inject

class ScheduleCacheGatewayImpl @Inject constructor(realm: Realm) : ScheduleCacheGateway