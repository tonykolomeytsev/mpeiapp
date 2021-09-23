package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kekmech.ru.common_cache.in_memory_cache.InMemoryCache
import kekmech.ru.common_shared_preferences.boolean
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_app_settings.dto.ContributorsItem
import kekmech.ru.domain_app_settings.dto.GitHubUser

class AppSettingsRepository(
    preferences: SharedPreferences,
    inMemoryCache: InMemoryCache,
    private val gitHubService: GitHubService,
) : AppSettings {

    private val contributorsCache = inMemoryCache
        .of<List<GitHubUser>>(
            key = CONTRIBUTORS_CACHE_KEY,
            keepAlways = false,
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

    fun observeContributors(): Observable<List<GitHubUser>> = contributorsCache.observe()

    fun fetchContributors(): Completable =
        gitHubService.getContributors()
            .flattenAsObservable { it.sortedByDescending(ContributorsItem::total) }
            .concatMapSingle { gitHubService.getUser(it.author.login) }
            .map { it.copy(bio = it.bio?.trim()) }
            .toList()
            .doOnSuccess(contributorsCache::set)
            .ignoreElement()

    companion object {

        private const val CONTRIBUTORS_CACHE_KEY = "CONTRIBUTORS_CACHE_KEY"
    }
}