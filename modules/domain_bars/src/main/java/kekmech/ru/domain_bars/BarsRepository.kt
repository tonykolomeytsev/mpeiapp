package kekmech.ru.domain_bars

import android.content.res.Resources
import android.util.Base64
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kekmech.ru.domain_bars.dto.JsKit
import kekmech.ru.domain_bars.dto.RawMarksResponse
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import kekmech.ru.domain_bars.dto.UserBarsInfo
import java.util.concurrent.TimeUnit

class BarsRepository(
    private val barsService: BarsService,
    private val gson: Gson,
    resources: Resources
) {
    private val userBarsCache = BehaviorSubject.createDefault(UserBarsInfo())
    private val extractJs = resources.openRawResource(R.raw.extract).bufferedReader().readText()

    fun getRemoteBarsConfig(): Single<RemoteBarsConfig> = Single.just(
        RemoteBarsConfig(
            loginUrl = "https://bars.mpei.ru/bars_web",
            studentListUrl = "https://bars.mpei.ru/bars_web/Student/ListStudent",
            marksListUrl = "https://bars.mpei.ru/bars_web/Student/Part1",
            logoutUrl = "https://bars.mpei.ru/bars_web/Auth/Exit",
            js = JsKit(
                extractDataEncoded = Base64.encodeToString(extractJs.toByteArray(), Base64.NO_WRAP),
                changeSemesterEncoded = ""
            )
        )
    )

    fun observeUserBars(): Observable<UserBarsInfo> = userBarsCache
        .distinctUntilChanged()
        .debounce(INFO_CHANGING_DEBOUNCE, TimeUnit.MILLISECONDS)

    fun pushMarksJson(marksJson: String): Completable = updateUserBarsCache {
        val rawMarksResponse = gson.fromJson(marksJson, RawMarksResponse::class.java)
        copy(assessedDisciplines = RawToMarksResponseMapper.map(rawMarksResponse).payload)
    }

    fun pushStudentName(studentName: String): Completable = updateUserBarsCache {
        val studentNames = studentName.split("\\s+".toRegex())
        if (studentNames.size > 2) {
            copy(name = "${studentNames[0]} ${studentNames[1]}")
        } else {
            copy(name = studentName)
        }
    }

    fun pushStudentGroup(studentGroup: String): Completable = updateUserBarsCache {
        copy(group = studentGroup)
    }

    private fun updateUserBarsCache(updater: UserBarsInfo.() -> UserBarsInfo) = userBarsCache
        .firstOrError()
        .map { updater(it) }
        .doOnSuccess { userBarsCache.onNext(updater(it)) }
        .doOnError { userBarsCache.onError(it) }
        .ignoreElement()

    companion object {
        private const val INFO_CHANGING_DEBOUNCE = 150L
    }
}