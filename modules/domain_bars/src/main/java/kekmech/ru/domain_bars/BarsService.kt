package kekmech.ru.domain_bars

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import okhttp3.ResponseBody
import retrofit2.http.GET

@EndpointUrl(BackendServiceUrl.BARS)
interface BarsService {

    @GET("config.json")
    fun getRemoteBarsConfig(): Single<RemoteBarsConfig>

    @GET("extract.js")
    fun getExtractJs(): Single<ResponseBody>
}