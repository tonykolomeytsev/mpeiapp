package kekmech.ru.feature_app_settings_api.data

import kekmech.ru.lib_network.AppEnvironment

public interface AppEnvironmentRepository {

    public fun getAppEnvironment(): AppEnvironment

    public fun setAppEnvironment(appEnvironment: AppEnvironment)
}
