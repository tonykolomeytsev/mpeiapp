package kekmech.ru.feature_app_settings_api.data

import kekmech.ru.library_network.AppEnvironment

interface AppEnvironmentRepository {

    fun getAppEnvironment(): AppEnvironment

    fun setAppEnvironment(appEnvironment: AppEnvironment)
}
