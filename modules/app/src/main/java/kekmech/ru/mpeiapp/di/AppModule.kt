package kekmech.ru.mpeiapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.redmadrobot.mapmemory.MapMemory
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.lib_analytics_android.FirebaseAnalyticsProvider
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.lib_feature_toggles.RemoteConfigWrapper
import kekmech.ru.lib_feature_toggles.RemoteVariable
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
        *FeatureModules,
        *LibraryModules,
    )
}

private class RemoteConfigWrapperImpl : RemoteConfigWrapper {

    private val remoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    override fun getUntyped(name: String): String = remoteConfig.getString(name)

    override fun getAll(): Map<String, String> =
        remoteConfig.all.mapValues { (_, v) -> v.asString() }
}
