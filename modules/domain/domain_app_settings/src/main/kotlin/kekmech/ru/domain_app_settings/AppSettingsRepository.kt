package kekmech.ru.domain_app_settings

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_shared_preferences.boolean
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_app_settings.dto.ContributorsCacheWrapper
import kekmech.ru.domain_app_settings.dto.ContributorsItem
import kekmech.ru.domain_app_settings.dto.GitHubUser
import kekmech.ru.domain_app_settings_models.AppEnvironment
import kekmech.ru.domain_app_settings_models.AppSettings
import java.time.Duration

class AppSettingsRepository(
    preferences: SharedPreferences,
    persistentCache: PersistentCache,
    private val gitHubService: GitHubService,
) {

    private val contributorsCache = persistentCache
        .of(
            key = CONTRIBUTORS_CACHE_KEY,
            valueClass = ContributorsCacheWrapper::class.java,
            lifetime = Duration.ofDays(1)
        )

    private var isDarkThemeEnabled by preferences.boolean("app_is_dark_theme_enabled", false)
    private var isSnowEnabled by preferences.boolean("app_is_snow_enabled", true)
    private var showNavigationButton by preferences.boolean("show_nav_fab", true)
    private var autoHideBottomSheet by preferences.boolean("map_auto_hide_bottom_sheet", true)
    private var appEnvironment by preferences.string("app_env", "PROD")
    private var languageCode: String by preferences.string("app_lang", "ru_RU")
    private var mapAppearanceType: String by preferences.string("app_map_type", "hybrid")

    fun getAppSettings(): Single<AppSettings> = Single.fromCallable {
        AppSettings(
            isDarkThemeEnabled = isDarkThemeEnabled,
            isSnowEnabled = isSnowEnabled,
            languageCode = languageCode,
            showNavigationButton = showNavigationButton,
            autoHideBottomSheet = autoHideBottomSheet,
            mapAppearanceType = mapAppearanceType,
            appEnvironment = runCatching { AppEnvironment.valueOf(appEnvironment) }
                .getOrDefault(AppEnvironment.PROD),
        )
    }

    fun changeAppSettings(transform: AppSettings.() -> AppSettings): Single<AppSettings> =
        getAppSettings()
            .map(transform)
            .doOnSuccess {
                isDarkThemeEnabled = it.isDarkThemeEnabled
                isSnowEnabled = it.isSnowEnabled
                showNavigationButton = it.showNavigationButton
                autoHideBottomSheet = it.autoHideBottomSheet
                appEnvironment = it.appEnvironment.toString()
                languageCode = it.languageCode
                mapAppearanceType = it.mapAppearanceType
            }

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
