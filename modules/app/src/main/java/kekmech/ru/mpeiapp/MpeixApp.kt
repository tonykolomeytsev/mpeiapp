package kekmech.ru.mpeiapp

import android.app.Application
import android.content.Context
import android.os.Build
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kekmech.ru.feature_app_settings_api.data.AppEnvironmentRepository
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import kekmech.ru.lib_elm.TimberLogger
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.di.RouterHolder
import kekmech.ru.lib_network.ServiceUrlResolver
import kekmech.ru.mpeiapp.di.AppModule
import org.koin.android.ext.android.getKoin
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
    private val appEnvironmentRepository by inject<AppEnvironmentRepository>()
    private val appLifecycleObservers: List<AppLifecycleObserver> by lazy { getKoin().getAll() }

    @Suppress("MagicNumber")
    override fun onCreate() {
        super.onCreate()
        initKoin()
        appLifecycleObservers.forEach { it.onCreate(applicationContext) }

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
        ServiceUrlResolver.setAppEnvironment(appEnvironmentRepository.getAppEnvironment())
        RemoteConfig.setup()
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
        modules(listOf(AppModule))
        modules(DebugModules)
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

