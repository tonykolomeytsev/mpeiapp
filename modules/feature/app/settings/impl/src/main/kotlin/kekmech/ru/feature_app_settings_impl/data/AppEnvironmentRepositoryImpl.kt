package kekmech.ru.feature_app_settings_impl.data

import android.content.SharedPreferences
import kekmech.ru.feature_app_settings_api.data.AppEnvironmentRepository
import kekmech.ru.lib_network.AppEnvironment

internal class AppEnvironmentRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : AppEnvironmentRepository {

    override fun getAppEnvironment(): AppEnvironment =
        runCatching {
            AppEnvironment.valueOf(
                sharedPreferences.getString(APP_ENVIRONMENT_KEY, "PROD").orEmpty()
            )
        }.getOrDefault(AppEnvironment.PROD)

    override fun setAppEnvironment(appEnvironment: AppEnvironment) {
        sharedPreferences.edit().putString(APP_ENVIRONMENT_KEY, appEnvironment.name).apply()
    }

    private companion object {

        const val APP_ENVIRONMENT_KEY = "app_env"
    }
}
