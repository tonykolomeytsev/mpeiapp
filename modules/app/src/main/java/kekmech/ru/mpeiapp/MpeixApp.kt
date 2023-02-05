package kekmech.ru.mpeiapp

import android.app.Application
import android.content.Context
import android.os.Build
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kekmech.ru.common_analytics.di.AnalyticsModule
import kekmech.ru.common_app_database.di.AppDatabaseModule
import kekmech.ru.common_cache.di.CacheModule
import kekmech.ru.common_di.modules
import kekmech.ru.common_feature_toggles.di.CommonFeatureTogglesModule
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.TimberLogger
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.di.NavigationModule
import kekmech.ru.common_navigation.di.RouterHolder
import kekmech.ru.common_network.di.NetworkModule
import kekmech.ru.common_network.retrofit.ServiceUrlResolver
import kekmech.ru.feature_app_settings.di.AppSettingsModule
import kekmech.ru.feature_bars.di.BarsModule
import kekmech.ru.feature_dashboard.di.DashboardModule
import kekmech.ru.feature_force_update.di.ForceUpdateModule
import kekmech.ru.feature_map.di.MapModule
import kekmech.ru.feature_notes.di.NotesModule
import kekmech.ru.feature_onboarding.di.OnboardingModule
import kekmech.ru.feature_schedule.di.ScheduleModule
import kekmech.ru.feature_search.di.SearchFeatureModule
import kekmech.ru.mpeiapp.deeplink.di.DeeplinkModule
import kekmech.ru.mpeiapp.di.AppModule
import kekmech.ru.mpeiapp.ui.main.di.MainScreenModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree
import vivid.money.elmslie.android.logger.strategy.Crash
import vivid.money.elmslie.core.config.ElmslieConfig

class MpeixApp : Application(),
    RouterHolder {

    override val router by inject<Router>()
    private val sharedPreferences by fastLazy { applicationContext.getSharedPreferences("mpeix", MODE_PRIVATE) }
    private val isDebugBackendEnvironmentEnabled by fastLazy {
        sharedPreferences.getBoolean("is_debug_env", false)
    }

    @Suppress("MagicNumber")
    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                // Merely log undeliverable exceptions
                Timber.e(e)
            } else {
                // Forward all others to current thread's uncaught exception handler
                Thread.currentThread().also { thread ->
                    thread.uncaughtExceptionHandler?.uncaughtException(thread, e)
                }
            }
        }
        ServiceUrlResolver.setEnvironment(debug = isDebugBackendEnvironmentEnabled)
        RemoteConfig.setup()
        initKoin()
        initTimber()
        if (Build.VERSION.SDK_INT < 25) LocaleContextWrapper.updateResourcesV24(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleContextWrapper.wrapContext(base))
    }

    private fun initKoin() = startKoin {
        androidLogger()
        androidContext(this@MpeixApp)
        allowOverride(false)
        modules(listOf(
            AppModule,
            MainScreenModule,
            DeeplinkModule,
            // commons
            NavigationModule,
            NetworkModule,
            CacheModule,
            AppDatabaseModule,
            AnalyticsModule,
            CommonFeatureTogglesModule,
            // features
            OnboardingModule,
            DashboardModule,
            ScheduleModule,
            AppSettingsModule,
            MapModule,
            NotesModule,
            ForceUpdateModule,
            BarsModule,
            SearchFeatureModule
        ))
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant()
        }
        ElmslieConfig.logger {
            if (BuildConfig.DEBUG) {
                fatal(Crash)
                nonfatal(TimberLogger.E)
                debug(TimberLogger.D)
            } else {
                fatal(TimberLogger.E)
            }
        }
    }

    @Suppress("NestedBlockDepth")
    override fun getPackageName(): String {
        try {
            val stackTrace = Thread.currentThread().stackTrace
            for (element in stackTrace) {
                if ("org.chromium.base.BuildInfo".equals(element.className, true)) {
                    if ("getAll".equals(element.methodName, ignoreCase = true)) {
                        return "com.android.chrome"
                    }
                    break
                }
            }
        } catch (_: Exception) {
            Timber.d("Replace app package name")
        }
        return super.getPackageName()
    }
}

