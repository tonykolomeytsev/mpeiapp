package kekmech.ru.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.dto.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userCacheGateway: UserCacheGateway,
    private val context: Context
) : UserRepository {

    private val sharedPreferences = context.getSharedPreferences("mpeix", MODE_PRIVATE)

    override var savedUpdateUrl: String = ""
    override var savedUpdateDescription: String = ""
    override var appLaunchCount: Int
        get() = sharedPreferences.getInt("launch_count", 0)
        set(value) {
            sharedPreferences
                .edit()
                .putInt("launch_count", value)
                .apply()
        }

    override fun get(refresh: Boolean): User {
        val user = userCacheGateway.get()
        if (user == null) userCacheGateway.set(User.defaultUser())
        return userCacheGateway.get()!!
    }

}

