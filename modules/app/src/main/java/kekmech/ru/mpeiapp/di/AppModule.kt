package kekmech.ru.mpeiapp.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.common_analytics.di.AnalyticsModule
import kekmech.ru.common_app_database.di.AppDatabaseModule
import kekmech.ru.common_cache.di.CacheModule
import kekmech.ru.common_di.AppVersionName
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_di.loadKoinModules
import kekmech.ru.common_feature_toggles.RemoteConfigWrapper
import kekmech.ru.common_feature_toggles.di.CommonFeatureTogglesModule
import kekmech.ru.common_navigation.di.NavigationModule
import kekmech.ru.common_network.di.NetworkModule
import kekmech.ru.domain_favorite_schedule.di.DomainFavoriteScheduleModule
import kekmech.ru.domain_github.di.DomainGitHubModule
import kekmech.ru.feature_app_settings.di.AppSettingsModule
import kekmech.ru.feature_bars.di.BarsModule
import kekmech.ru.feature_dashboard.di.DashboardModule
import kekmech.ru.feature_force_update.di.ForceUpdateModule
import kekmech.ru.feature_map.di.MapModule
import kekmech.ru.feature_notes.di.NotesModule
import kekmech.ru.feature_onboarding.di.OnboardingModule
import kekmech.ru.feature_schedule.di.ScheduleModule
import kekmech.ru.feature_search.di.SearchFeatureModule
import kekmech.ru.mpeiapp.BuildConfig
import kekmech.ru.mpeiapp.Prefetcher
import kekmech.ru.mpeiapp.deeplink.di.DeeplinkModule
import kekmech.ru.mpeiapp.ui.main.di.MainScreenModule
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import java.io.File
import java.util.Locale

object AppModule : ModuleProvider({
    single { Locale.GERMAN } bind Locale::class
    single { Prefetcher(get()) } bind Prefetcher::class
    single { FirebaseAnalytics.getInstance(androidApplication()) } bind FirebaseAnalytics::class
    factory(named("appCacheDir")) {
        File(androidApplication().cacheDir, "persistent")
    }
    factory(named("firebaseRemoteConfigWrapper")) { RemoteConfigWrapperImpl() } bind RemoteConfigWrapper::class
    factory { AppVersionName(BuildConfig.VERSION_NAME) }

    loadKoinModules(
        listOf(
            MainScreenModule,
            DeeplinkModule,
            // common
            NavigationModule,
            NetworkModule,
            CacheModule,
            AppDatabaseModule,
            AnalyticsModule,
            CommonFeatureTogglesModule,
            // domain
            DomainGitHubModule,
            DomainFavoriteScheduleModule,
            // feature
            OnboardingModule,
            DashboardModule,
            ScheduleModule,
            AppSettingsModule,
            MapModule,
            NotesModule,
            ForceUpdateModule,
            BarsModule,
            SearchFeatureModule
        )
    )
})

private class RemoteConfigWrapperImpl : RemoteConfigWrapper {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    override fun get(featureToggleKey: String): Boolean = remoteConfig.getBoolean(featureToggleKey)
}
