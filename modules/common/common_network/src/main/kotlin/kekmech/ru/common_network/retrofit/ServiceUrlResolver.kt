package kekmech.ru.common_network.retrofit

import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.domain_app_settings_models.AppEnvironment

public object ServiceUrlResolver {

    private var appEnvironment: AppEnvironment = AppEnvironment.PROD

    public fun setAppEnvironment(appEnvironment: AppEnvironment) {
        this.appEnvironment = appEnvironment
    }

    public fun resolve(url: BackendServiceUrl): String =
        when (appEnvironment) {
            AppEnvironment.PROD -> url.prodEndpoint
            AppEnvironment.STAGING -> url.stagingEndpoint
            AppEnvironment.MOCK -> url.mockEndpoint
        }
}
