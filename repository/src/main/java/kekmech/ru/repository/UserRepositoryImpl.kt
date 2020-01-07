package kekmech.ru.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import kekmech.ru.core.dto.User
import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.UserRepository

class UserRepositoryImpl constructor(
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
    override var mapState: Int = 0

    override var isShowedUpdateDialog: Boolean = true

    override fun get(refresh: Boolean): User {
        val user = userCacheGateway.get()
        if (user == null) userCacheGateway.set(User.defaultUser())
        return userCacheGateway.get()!!
    }

    override var isDarkThemeEnabled: Boolean
        get() = sharedPreferences.getBoolean("dark_theme", false)
        set(value) { sharedPreferences.edit().putBoolean("dark_theme", value).apply() }

}

