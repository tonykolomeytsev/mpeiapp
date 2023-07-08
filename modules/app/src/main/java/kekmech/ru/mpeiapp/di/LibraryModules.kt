package kekmech.ru.mpeiapp.di

import kekmech.ru.lib_analytics_android.di.LibraryAnalyticsModule
import kekmech.ru.lib_app_database.di.LibraryAppDatabaseModule
import kekmech.ru.lib_coroutines.di.LibraryCoroutinesModule
import kekmech.ru.lib_feature_toggles.di.LibraryFeatureTogglesModule
import kekmech.ru.lib_navigation.di.LibraryNavigationModule
import kekmech.ru.lib_network.di.LibraryNetworkModule
import kekmech.ru.lib_persistent_cache.di.LibraryPersistentCacheModule

internal val LibraryModules = arrayOf(
    LibraryCoroutinesModule,
    LibraryPersistentCacheModule,
    LibraryAppDatabaseModule,
    LibraryAnalyticsModule,
    LibraryNetworkModule,
    LibraryFeatureTogglesModule,
    LibraryNavigationModule,
)
