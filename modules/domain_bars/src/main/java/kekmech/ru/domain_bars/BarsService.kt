package kekmech.ru.domain_bars

import io.reactivex.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import retrofit2.http.GET


@EndpointUrl(BackendServiceUrl.BARS)
interface BarsService {

    @GET("getRemoteBarsConfig")
    fun getRemoteBarsConfig(): Single<RemoteBarsConfig>
}