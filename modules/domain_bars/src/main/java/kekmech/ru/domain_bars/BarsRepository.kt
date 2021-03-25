package kekmech.ru.domain_bars

import io.reactivex.Single
import kekmech.ru.domain_bars.dto.JsKit
import kekmech.ru.domain_bars.dto.RemoteBarsConfig

class BarsRepository(
    private val barsService: BarsService
) {

    fun getRemoteBarsConfig(): Single<RemoteBarsConfig> = Single.just(
        RemoteBarsConfig(
            loginLink = "https://bars.mpei.ru/bars_web",
            studentListLink = "https://bars.mpei.ru/bars_web/Student/ListStudent",
            js = JsKit(
                extractMarks = "",
                changeSemester = ""
            )
        )
    )
}