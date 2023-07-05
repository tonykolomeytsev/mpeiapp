package kekmech.ru.lib_network

object ServiceUrlResolver {

    private var appEnvironment: AppEnvironment = AppEnvironment.PROD

    fun setAppEnvironment(appEnvironment: AppEnvironment) {
        ServiceUrlResolver.appEnvironment = appEnvironment
    }

    fun resolve(url: BackendServiceUrl): String =
        when (appEnvironment) {
            AppEnvironment.PROD -> url.prodEndpoint
            AppEnvironment.STAGING -> url.stagingEndpoint
            AppEnvironment.MOCK -> url.mockEndpoint
        }
}
