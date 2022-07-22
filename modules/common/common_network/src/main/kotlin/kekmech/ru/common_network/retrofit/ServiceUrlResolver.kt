package kekmech.ru.common_network.retrofit

import kekmech.ru.common_annotations.BackendServiceUrl

object ServiceUrlResolver {

    private var isDebugEnvironment: Boolean = false

    fun setEnvironment(debug: Boolean) {
        isDebugEnvironment = debug
    }

    fun resolve(url: BackendServiceUrl): String =
        if (isDebugEnvironment) url.devEndpoint else url.prodEndpoint
}
