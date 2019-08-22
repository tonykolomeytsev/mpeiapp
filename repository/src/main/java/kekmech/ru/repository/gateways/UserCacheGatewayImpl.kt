package kekmech.ru.repository.gateways

import io.realm.Realm
import kekmech.ru.core.dto.User
import kekmech.ru.core.gateways.UserCacheGateway
import javax.inject.Inject

class UserCacheGatewayImpl @Inject constructor(val realm: Realm) : UserCacheGateway {

    override fun get(): User {
        var user: User? = null
        realm.executeTransaction {
            user = it.where(User::class.java).findFirst()
            if (user == null) user = User()
            initDefault(user!!)
            it.insertOrUpdate(user!!)
        }
        return user!!
    }

    private fun initDefault(user: User) {
        user.groupName = ""
        user.firstLaunchFlag = false
        user.developerMode = false
        user.nightMode = false
    }

}