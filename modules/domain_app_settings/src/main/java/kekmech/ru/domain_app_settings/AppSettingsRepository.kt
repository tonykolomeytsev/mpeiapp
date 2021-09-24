package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_shared_preferences.boolean
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_app_settings.dto.ContributorsCacheWrapper
import kekmech.ru.domain_app_settings.dto.ContributorsItem
import kekmech.ru.domain_app_settings.dto.GitHubUser
import java.time.Duration

class AppSettingsRepository(
    preferences: SharedPreferences,
    persistentCache: PersistentCache,
    private val gitHubService: GitHubService,
) : AppSettings {

    private val contributorsCache = persistentCache
        .of(
            key = CONTRIBUTORS_CACHE_KEY,
            valueClass = ContributorsCacheWrapper::class.java,
            lifetime = Duration.ofDays(1)
        )

    override var isDarkThemeEnabled by preferences.boolean("app_is_dark_theme_enabled", false)

    override var isSnowEnabled by preferences.boolean("app_is_snow_enabled", true)

    override var showNavigationButton by preferences.boolean("show_nav_fab", true)

    override var autoHideBottomSheet by preferences.boolean("map_auto_hide_bottom_sheet", true)

    override var isDebugEnvironment by preferences.boolean("is_debug_env", false)

    override var languageCode: String by preferences.string("app_lang", "ru_RU")

    override var mapAppearanceType: String by preferences.string("app_map_type", "hybrid")

    fun complete(runnable: AppSettingsRepository.() -> Unit): Completable =
        Completable.fromRunnable { runnable.invoke(this) }

    fun getContributors(): Single<List<GitHubUser>> =
        contributorsCache.getOrError()
            .map { it.items }
            .onErrorResumeWith(
                fetchContributors()
                    .doOnSuccess { contributorsCache.set(ContributorsCacheWrapper(it)) }
            )

    private fun fetchContributors(): Single<List<GitHubUser>> =
        gitHubService.getContributors()
            .flattenAsObservable { it.sortedByDescending(ContributorsItem::total) }
            .concatMapSingle { gitHubService.getUser(it.author.login) }
            .map { it.copy(bio = it.bio?.trim()) }
            .toList()

    companion object {

        private const val CONTRIBUTORS_CACHE_KEY = "CONTRIBUTORS_CACHE_KEY"
    }
}