package kekmech.ru.domain_bars

import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import okhttp3.ResponseBody
import retrofit2.http.GET

@EndpointUrl(BackendServiceUrl.BARS)
internal interface BarsService {

    @GET("config.json")
    suspend fun getRemoteBarsConfig(): RemoteBarsConfig

    @GET("extract.js")
    suspend fun getExtractJs(): ResponseBody
}
