package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import kekmech.ru.domain_app_settings_models.AppEnvironment

class AppEnvironmentRepository(
    private val sharedPreferences: SharedPreferences,
) {

    fun getAppEnvironment(): AppEnvironment =
        runCatching {
            AppEnvironment.valueOf(
                sharedPreferences.getString(APP_ENVIRONMENT_KEY, "PROD").orEmpty()
            )
        }.getOrDefault(AppEnvironment.PROD)

    fun setAppEnvironment(appEnvironment: AppEnvironment) {
        sharedPreferences.edit().putString(APP_ENVIRONMENT_KEY, appEnvironment.name).apply()
    }

    private companion object {

        const val APP_ENVIRONMENT_KEY = "app_env"
    }
}
