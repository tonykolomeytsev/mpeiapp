package kekmech.ru.lib_network

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(value = AnnotationRetention.RUNTIME)
annotation class EndpointUrl(val value: BackendServiceUrl)
