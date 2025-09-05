package kekmech.ru.common_annotations

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(value = AnnotationRetention.RUNTIME)
public annotation class EndpointUrl(val value: BackendServiceUrl)
