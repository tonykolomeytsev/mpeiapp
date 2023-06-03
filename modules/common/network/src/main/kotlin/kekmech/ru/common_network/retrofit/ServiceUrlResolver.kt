package kekmech.ru.common_network.retrofit

import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.domain_app_settings_models.AppEnvironment

object ServiceUrlResolver {

    private var appEnvironment: AppEnvironment = AppEnvironment.PROD

    fun setAppEnvironment(appEnvironment: AppEnvironment) {
        this.appEnvironment = appEnvironment
    }

    fun resolve(url: BackendServiceUrl): String =
        when (appEnvironment) {
            AppEnvironment.PROD -> url.prodEndpoint
            AppEnvironment.STAGING -> url.stagingEndpoint
            AppEnvironment.MOCK -> url.mockEndpoint
        }
}
