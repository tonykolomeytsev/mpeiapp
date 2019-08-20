package kekmech.ru.repository.gateways

import io.realm.Realm
import kekmech.ru.core.CacheGateway
import kekmech.ru.core.InternetGateway

class InternetGatewayImpl(realm: Realm) : InternetGateway