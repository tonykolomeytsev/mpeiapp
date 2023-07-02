package kekmech.ru.domain_bars

import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import kekmech.ru.library_network.BackendServiceUrl
import kekmech.ru.library_network.EndpointUrl
import okhttp3.ResponseBody
import retrofit2.http.GET

@EndpointUrl(BackendServiceUrl.BARS)
internal interface BarsService {

    @GET("config.json")
    suspend fun getRemoteBarsConfig(): RemoteBarsConfig

    @GET("extract.js")
    suspend fun getExtractJs(): ResponseBody
}
