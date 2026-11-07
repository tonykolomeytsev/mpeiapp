package kekmech.ru.mpeiapp.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.lib_analytics_android.FirebaseAnalyticsProvider
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.lib_feature_toggles.RemoteConfigWrapper
import kekmech.ru.mpeiapp.BuildConfig
import kekmech.ru.mpeiapp.deeplink.di.DeeplinkModule
import kekmech.ru.mpeiapp.ui.main.di.MainScreenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.Locale

val AppModule = module {
    single { Locale.GERMAN } bind Locale::class
    factoryOf(::FirebaseAnalyticsProviderImpl) bind FirebaseAnalyticsProvider::class
    factoryOf(::RemoteConfigWrapperImpl) bind RemoteConfigWrapper::class
    factory { AppVersionName(BuildConfig.VERSION_NAME) }

    @Suppress("SpreadOperator")
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
