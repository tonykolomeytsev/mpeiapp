package kekmech.ru.lib_network

public object ServiceUrlResolver {

    private var appEnvironment: AppEnvironment = AppEnvironment.PROD

    public fun setAppEnvironment(appEnvironment: AppEnvironment) {
        ServiceUrlResolver.appEnvironment = appEnvironment
    }

    public fun resolve(url: BackendServiceUrl): String =
        when (appEnvironment) {
            AppEnvironment.PROD -> url.prodEndpoint
            AppEnvironment.STAGING -> url.stagingEndpoint
            AppEnvironment.MOCK -> url.mockEndpoint
        }
}
