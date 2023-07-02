package kekmech.ru.library_network

import retrofit2.Retrofit

inline fun <reified T : Any> Retrofit.Builder.buildApi(): T =
    baseUrl(getServiceUrl(T::class.java))
        .build()
        .create(T::class.java)

fun getServiceUrl(serviceClass: Class<*>): String {
    val annotation = serviceClass.getAnnotation(EndpointUrl::class.java)
        ?: error("$serviceClass should have @EndpointUrl annotation")

    return ServiceUrlResolver.resolve(annotation.value)
}
