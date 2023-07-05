package kekmech.ru.mpeiapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.redmadrobot.mapmemory.MapMemory
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_app_settings_impl.di.FeatureAppSettingsModule
import kekmech.ru.feature_app_update_impl.di.FeatureForceUpdateModule
import kekmech.ru.feature_bars_impl.di.FeatureBarsModule
import kekmech.ru.feature_contributors_impl.di.FeatureContributorsModule
import kekmech.ru.feature_dashboard_impl.di.FeatureDashboardModule
import kekmech.ru.feature_favorite_schedule_impl.di.FeatureFavoriteScheduleModule
import kekmech.ru.feature_map_impl.di.FeatureMapModule
import kekmech.ru.feature_notes_impl.di.FeatureNotesModule
import kekmech.ru.feature_onboarding_impl.di.FeatureOnboardingModule
import kekmech.ru.feature_schedule_impl.di.FeatureScheduleModule
import kekmech.ru.feature_search_impl.di.FeatureSearchFeatureModule
import kekmech.ru.lib_analytics_android.FirebaseAnalyticsProvider
import kekmech.ru.lib_analytics_android.di.LibraryAnalyticsModule
import kekmech.ru.lib_app_database.di.LibraryAppDatabaseModule
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.lib_coroutines.di.LibraryCoroutinesModule
import kekmech.ru.lib_feature_toggles.RemoteConfigWrapper
import kekmech.ru.lib_feature_toggles.RemoteVariable
import kekmech.ru.lib_feature_toggles.di.LibraryFeatureTogglesModule
import kekmech.ru.lib_navigation.di.LibraryNavigationModule
import kekmech.ru.lib_network.di.LibraryNetworkModule
import kekmech.ru.lib_persistent_cache.di.LibraryPersistentCacheModule
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
        FeatureFavoriteScheduleModule,
        FeatureContributorsModule,
        // libraries
        LibraryCoroutinesModule,
        LibraryPersistentCacheModule,
        LibraryAppDatabaseModule,
        LibraryAnalyticsModule,
        LibraryNetworkModule,
        LibraryFeatureTogglesModule,
        LibraryNavigationModule,
    )
}

private class RemoteConfigWrapperImpl : RemoteConfigWrapper {

    private val remoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    override fun getUntyped(name: String): String = remoteConfig.getString(name)

    override fun getAll(): Map<String, String> =
        remoteConfig.all.mapValues { (_, v) -> v.asString() }
}
