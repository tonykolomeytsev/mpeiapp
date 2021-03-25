package kekmech.ru.domain_bars

import android.content.res.Resources
import io.reactivex.Single
import kekmech.ru.domain_bars.dto.JsKit
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import org.intellij.lang.annotations.Language

class BarsRepository(
    private val barsService: BarsService,
    resources: Resources
) {
    @Language("")
    private val extractJs = resources.openRawResource(R.raw.extract).bufferedReader().readText()

    fun getRemoteBarsConfig(): Single<RemoteBarsConfig> = Single.just(
        RemoteBarsConfig(
            loginLink = "https://bars.mpei.ru/bars_web",
            studentListLink = "https://bars.mpei.ru/bars_web/Student/ListStudent",
            js = JsKit(
                extractData = extractJs,
                changeSemester = ""
            )
        )
    )
}