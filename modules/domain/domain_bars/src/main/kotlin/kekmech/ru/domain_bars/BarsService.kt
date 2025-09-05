package kekmech.ru.domain_bars

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import okhttp3.ResponseBody
import retrofit2.http.GET

@EndpointUrl(value = BackendServiceUrl.BARS)
public interface BarsService {

    @GET("config.json")
    public fun getRemoteBarsConfig(): Single<RemoteBarsConfig>

    @GET("extract.js")
    public fun getExtractJs(): Single<ResponseBody>
}
