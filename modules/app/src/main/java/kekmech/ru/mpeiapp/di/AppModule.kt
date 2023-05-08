package kekmech.ru.mpeiapp.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.common_analytics.FirebaseAnalyticsProvider
import kekmech.ru.common_analytics.di.CommonAnalyticsModule
import kekmech.ru.common_app_database.di.CommonAppDatabaseModule
import kekmech.ru.common_cache.di.CommonCacheModule
import kekmech.ru.common_di.AppCacheDir
import kekmech.ru.common_di.AppVersionName
import kekmech.ru.common_feature_toggles.RemoteConfigWrapper
import kekmech.ru.common_feature_toggles.di.CommonFeatureTogglesModule
import kekmech.ru.common_navigation.di.CommonNavigationModule
import kekmech.ru.common_network.di.CommonNetworkModule
import kekmech.ru.domain_dashboard.di.DomainDashboardModule
import kekmech.ru.domain_favorite_schedule.di.DomainFavoriteScheduleModule
import kekmech.ru.domain_github.di.DomainGitHubModule
import kekmech.ru.domain_notes.di.DomainNotesModule
import kekmech.ru.domain_schedule.di.DomainScheduleModule
import kekmech.ru.feature_app_settings.di.FeatureAppSettingsModule
import kekmech.ru.feature_bars.di.FeatureBarsModule
import kekmech.ru.feature_dashboard.di.FeatureDashboardModule
import kekmech.ru.feature_force_update.di.FeatureForceUpdateModule
import kekmech.ru.feature_map.di.FeatureMapModule
import kekmech.ru.feature_notes.di.FeatureNotesModule
import kekmech.ru.feature_onboarding.di.FeatureOnboardingModule
import kekmech.ru.feature_schedule.di.FeatureScheduleModule
import kekmech.ru.feature_search.di.FeatureSearchFeatureModule
import kekmech.ru.mpeiapp.BuildConfig
import kekmech.ru.mpeiapp.Prefetcher
import kekmech.ru.mpeiapp.deeplink.di.DeeplinkModule
import kekmech.ru.mpeiapp.ui.main.di.MainScreenModule
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File
import java.util.Locale

val AppModule = module {
    single { Locale.GERMAN } bind Locale::class
    factoryOf(::Prefetcher)
    factoryOf(::FirebaseAnalyticsProviderImpl) bind FirebaseAnalyticsProvider::class
    factoryOf(::RemoteConfigWrapperImpl) bind RemoteConfigWrapper::class
    factory { AppVersionName(BuildConfig.VERSION_NAME) }
    factory {
        AppCacheDir(File(androidApplication().cacheDir, "persistent"))
    } bind AppCacheDir::class

    includes(
        MainScreenModule,
        DeeplinkModule,
        // common
        CommonAnalyticsModule,
        CommonAppDatabaseModule,
        CommonCacheModule,
        CommonFeatureTogglesModule,
        CommonNavigationModule,
        CommonNetworkModule,
        // domain
        DomainDashboardModule,
        DomainFavoriteScheduleModule,
        DomainGitHubModule,
        DomainNotesModule,
        DomainScheduleModule,
        // feature
        FeatureAppSettingsModule,
        FeatureBarsModule,
        FeatureDashboardModule,
        FeatureForceUpdateModule,
        FeatureMapModule,
        FeatureNotesModule,
        FeatureOnboardingModule,
        FeatureScheduleModule,
        FeatureSearchFeatureModule,
    )
}

private class RemoteConfigWrapperImpl : RemoteConfigWrapper {

    private val remoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    override fun get(featureToggleKey: String): Boolean = remoteConfig.getBoolean(featureToggleKey)
}
