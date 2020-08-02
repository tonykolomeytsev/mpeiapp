package kekmech.ru.common_annotations

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(value = AnnotationRetention.RUNTIME)
annotation class EndpointUrl(val value: String)