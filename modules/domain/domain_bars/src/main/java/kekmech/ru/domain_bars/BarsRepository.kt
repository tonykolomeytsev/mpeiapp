package kekmech.ru.domain_bars

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.in_memory_cache.InMemoryCache
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_bars.dto.RawMarksResponse
import kekmech.ru.domain_bars.dto.RawRatingResponse
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import kekmech.ru.domain_bars.dto.UserBarsInfo
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BarsRepository(
    private val barsService: BarsService,
    private val gson: Gson,
    preferences: SharedPreferences,
    persistentCache: PersistentCache,
    inMemoryCache: InMemoryCache,
) {

    private val userBarsCache = inMemoryCache
        .of<UserBarsInfo>(BARS_USER_INFO_KEY, keepAlways = false)
        .apply { set(UserBarsInfo()) }
    private val remoteBarsConfigCache = persistentCache
        .of(
            key = BARS_CONFIG_CACHE_KEY,
            valueClass = RemoteBarsConfig::class.java,
        )
    private val extractJsCache = persistentCache
        .of(
            key = BARS_JS_CACHE_KEY,
            valueClass = String::class.java,
        )
    var latestLoadedUrl by preferences.string("bars_last_loaded_link")

    fun getRemoteBarsConfig(): Single<RemoteBarsConfig> =
        barsService
            .getRemoteBarsConfig()
            .doOnSuccess(remoteBarsConfigCache::set)
            .onErrorResumeWith(remoteBarsConfigCache.get().toSingle())

    fun getExtractJs(): Single<String> =
        barsService
            .getExtractJs()
            .map { it.charStream().buffered().readText() }
            .doOnSuccess(extractJsCache::set)
            .onErrorResumeWith(extractJsCache.get().toSingle())

    fun observeUserBars(): Observable<UserBarsInfo> =
        userBarsCache
            .observe()
            .debounce(INFO_CHANGING_DEBOUNCE, TimeUnit.MILLISECONDS)

    fun pushMarksJson(marksJson: String): Completable =
        updateUserBarsCache {
            val rawMarksResponse = gson.fromJson(marksJson, RawMarksResponse::class.java)
            copy(assessedDisciplines = RawToMarksResponseMapper.map(rawMarksResponse).payload)
        }

    fun pushStudentName(studentName: String): Completable =
        updateUserBarsCache {
            val studentNames = studentName.split("\\s+".toRegex())
            if (studentNames.size > 2) {
                copy(name = "${studentNames[0]} ${studentNames[1]}")
            } else {
                copy(name = studentName)
            }
        }

    fun pushStudentGroup(studentGroup: String): Completable =
        updateUserBarsCache {
            copy(group = studentGroup)
        }

    fun pushStudentRating(ratingJson: String): Completable =
        updateUserBarsCache {
            val rawRatingResponse = gson.fromJson(ratingJson, RawRatingResponse::class.java)
            copy(rating = RawToRatingResponseMapper.map(rawRatingResponse))
        }

    private fun updateUserBarsCache(updater: UserBarsInfo.() -> UserBarsInfo): Completable =
        Completable.fromAction {
            synchronized(this@BarsRepository) {
                userBarsCache
                    .peek()
                    ?.let(updater)
                    ?.let(userBarsCache::set)
            }
        }
            .doOnError(Timber::e)

    companion object {

        private const val INFO_CHANGING_DEBOUNCE = 150L
        private const val BARS_USER_INFO_KEY = "BARS_USER_INFO_KEY"
        private const val BARS_CONFIG_CACHE_KEY = "BARS_CONFIG_CACHE_KEY"
        private const val BARS_JS_CACHE_KEY = "BARS_JS_CACHE_KEY"
    }
}
