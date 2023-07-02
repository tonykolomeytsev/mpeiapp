package kekmech.ru.mpeiapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.redmadrobot.mapmemory.MapMemory
import kekmech.ru.common_analytics.FirebaseAnalyticsProvider
import kekmech.ru.common_analytics.di.CommonAnalyticsModule
import kekmech.ru.common_di.AppVersionName
import kekmech.ru.domain_bars.di.DomainBarsModule
import kekmech.ru.domain_dashboard.di.DomainDashboardModule
import kekmech.ru.domain_favorite_schedule.di.DomainFavoriteScheduleModule
import kekmech.ru.domain_github.di.DomainGitHubModule
import kekmech.ru.domain_map.di.DomainMapModule
import kekmech.ru.domain_notes.di.DomainNotesModule
import kekmech.ru.domain_schedule.di.DomainScheduleModule
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_app_settings.di.FeatureAppSettingsModule
import kekmech.ru.feature_bars.di.FeatureBarsModule
import kekmech.ru.feature_dashboard.di.FeatureDashboardModule
import kekmech.ru.feature_force_update.di.FeatureForceUpdateModule
import kekmech.ru.feature_map.di.FeatureMapModule
import kekmech.ru.feature_notes.di.FeatureNotesModule
import kekmech.ru.feature_onboarding.di.FeatureOnboardingModule
import kekmech.ru.feature_schedule.di.FeatureScheduleModule
import kekmech.ru.feature_search.di.FeatureSearchFeatureModule
import kekmech.ru.library_app_database_impl.di.CommonAppDatabaseModule
import kekmech.ru.library_coroutines.di.LibraryCoroutinesModule
import kekmech.ru.library_feature_toggles.RemoteConfigWrapper
import kekmech.ru.library_feature_toggles.RemoteVariable
import kekmech.ru.library_feature_toggles.di.CommonFeatureTogglesModule
import kekmech.ru.library_navigation.di.CommonNavigationModule
import kekmech.ru.library_network.di.CommonNetworkModule
import kekmech.ru.library_persistent_cache.di.LibraryPersistentCacheModule
import kekmech.ru.mpeiapp.BuildConfig
import kekmech.ru.mpeiapp.ComposeEnabledFeatureToggle
import kekmech.ru.mpeiapp.deeplink.di.DeeplinkModule
import kekmech.ru.mpeiapp.ui.main.di.MainScreenModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.Locale

val AppModule = module {
    single { Locale.GERMAN } bind Locale::class
    factoryOf(::FirebaseAnalyticsProviderImpl) bind FirebaseAnalyticsProvider::class
    factoryOf(::RemoteConfigWrapperImpl) bind RemoteConfigWrapper::class
    factory { AppVersionName(BuildConfig.VERSION_NAME) }
    factoryOf(::ComposeEnabledFeatureToggle) bindIntoList RemoteVariable::class

    // TODO: figure out how to inject Dispatchers correctly
    @Suppress("RemoveExplicitTypeArguments", "InjectDispatcher")
    single<DataStore<Preferences>> {
        val applicationContext: Context = androidApplication()
        val name = "mpeix.datastore.preferences"
        /**
         * Same implementation as in [androidx.datastore.preferences.preferencesDataStore] delegate
         */
        PreferenceDataStoreFactory.create(
            corruptionHandler = null,
            migrations = listOf(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        ) {
            applicationContext.preferencesDataStoreFile(name)
        }
    }
    single { MapMemory() }

    includes(
        MainScreenModule,
        DeeplinkModule,
        // common
        CommonAnalyticsModule,
        CommonAppDatabaseModule,
        CommonFeatureTogglesModule,
        CommonNavigationModule,
        CommonNetworkModule,
        // domain
        DomainBarsModule,
        DomainDashboardModule,
        DomainFavoriteScheduleModule,
        DomainGitHubModule,
        DomainNotesModule,
        DomainScheduleModule,
        DomainMapModule,
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
        // libraries
        LibraryPersistentCacheModule,
        LibraryCoroutinesModule,
    )
}

private class RemoteConfigWrapperImpl : RemoteConfigWrapper {

    private val remoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    override fun getUntyped(name: String): String = remoteConfig.getString(name)

    override fun getAll(): Map<String, String> =
        remoteConfig.all.mapValues { (_, v) -> v.asString() }
}
