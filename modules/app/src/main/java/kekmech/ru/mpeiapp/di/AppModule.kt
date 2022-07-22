package kekmech.ru.mpeiapp.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_feature_toggles.RemoteConfigWrapper
import kekmech.ru.mpeiapp.Prefetcher
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import java.io.File
import java.util.*

object AppModule : ModuleProvider({
    single { Locale.GERMAN } bind Locale::class
    single { Prefetcher(get()) } bind Prefetcher::class
    single { FirebaseAnalytics.getInstance(androidApplication()) } bind FirebaseAnalytics::class
    factory(named("appCacheDir")) {
        File(androidApplication().cacheDir, "persistent")
    }
    factory(named("firebaseRemoteConfigWrapper")) { RemoteConfigWrapperImpl() } bind RemoteConfigWrapper::class
})

private class RemoteConfigWrapperImpl : RemoteConfigWrapper {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    override fun get(featureToggleKey: String): Boolean = remoteConfig.getBoolean(featureToggleKey)
}
