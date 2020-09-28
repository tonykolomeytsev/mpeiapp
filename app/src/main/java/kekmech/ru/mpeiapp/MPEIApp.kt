package kekmech.ru.mpeiapp

import android.app.Application
import kekmech.ru.common_analytics.di.AnalyticsModule
import kekmech.ru.common_android.di.CommonAndroidModule
import kekmech.ru.common_app_database.di.AppDatabaseModule
import kekmech.ru.common_cache.di.CacheModule
import kekmech.ru.common_di.modules
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.di.NavigationModule
import kekmech.ru.common_navigation.di.RouterHolder
import kekmech.ru.common_network.di.NetworkModule
import kekmech.ru.common_webview.di.WebViewModule
import kekmech.ru.feature_app_settings.di.AppSettingsModule
import kekmech.ru.feature_dashboard.di.DashboardModule
import kekmech.ru.feature_onboarding.di.OnboardingModule
import kekmech.ru.feature_schedule.di.ScheduleModule
import kekmech.ru.map.di.MapModule
import kekmech.ru.mpeiapp.di.AppModule
import kekmech.ru.mpeiapp.ui.main.di.MainScreenModule
import kekmech.ru.notes.di.NotesModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


class MPEIApp : Application(),
    RouterHolder {

    override val router by inject<Router>()

    override fun onCreate() {
        super.onCreate()
        RemoteConfig.setup()
        initKoin()
        initTimber()
    }

    private fun initKoin() = startKoin {
        androidLogger()
        androidContext(this@MPEIApp)
        modules(listOf(
            AppModule,
            MainScreenModule,
            // commons
            NavigationModule,
            CommonAndroidModule,
            NetworkModule,
            WebViewModule,
            CacheModule,
            AppDatabaseModule,
            AnalyticsModule,
            // features
            OnboardingModule,
            DashboardModule,
            ScheduleModule,
            AppSettingsModule,
            MapModule,
            NotesModule
        ))
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override fun getPackageName(): String {
        try {
            val stackTrace = Thread.currentThread().stackTrace
            for (element in stackTrace) {
                if ("org.chromium.base.BuildInfo".equals(element.className, true)) {
                    if ("getAll".equals(element.methodName, ignoreCase = true))
                        return ""
                    break
                }
            }
        } catch (e: Exception) { }
        return super.getPackageName()
    }
}
