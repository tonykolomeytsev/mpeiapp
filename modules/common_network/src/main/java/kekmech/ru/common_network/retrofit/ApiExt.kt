package kekmech.ru.common_network.retrofit

import kekmech.ru.common_annotations.EndpointUrl
import retrofit2.Retrofit

inline fun <reified T : Any> Retrofit.Builder.buildApi(): T =
    baseUrl(getServiceUrl(T::class.java))
        .build()
        .create(T::class.java)

fun getServiceUrl(serviceClass: Class<*>): String {
    val annotation = serviceClass.getAnnotation(EndpointUrl::class.java)
        ?: error("$serviceClass should have @EndpointUrl annotation")

    return annotation.value
}