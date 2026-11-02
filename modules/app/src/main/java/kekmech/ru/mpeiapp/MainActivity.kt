package kekmech.ru.mpeiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ComposeView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_main_screen_api.MainScreenLauncher
import kekmech.ru.feature_onboarding_api.OnboardingFeatureApi
import kekmech.ru.lib_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.NavigationHolder
import kekmech.ru.lib_navigation.Router
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandlersProcessor
import kekmech.ru.mpeiapp.ui.App
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import kekmech.ru.coreui.R as coreui_R

class MainActivity : AppCompatActivity() {

    private val navigationHolder: NavigationHolder by inject()
    private val appSettingsRepository: AppSettingsRepository by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val onboardingFeatureApi: OnboardingFeatureApi by inject()
    private val mainScreenLauncher: MainScreenLauncher by inject()
    private val deeplinkHandlersProcessor: DeeplinkHandlersProcessor by inject()
    private val mainActivityLifecycleObservers: List<MainActivityLifecycleObserver> by lazy {
        getKoin().getAll()
    }
    private val router: Router by inject()
    private val navEntries: List<NavEntries> by lazy { getKoin().getAll() }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        // Applied before super.onCreate, so the configuration is updated in place without recreation.
        // Compose screens read dark theme from configuration uiMode via isSystemInDarkTheme()
        delegate.localNightMode = if (appSettingsRepository.getAppSettings().isDarkThemeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        super.onCreate(savedInstanceState)
        mainActivityLifecycleObservers.forEach { it.onCreate(this) }

        setTheme()
        setContentView(R.layout.activity_main)

        savedInstanceState?.getBundle(KEY_NAVIGATION_STATE)?.let(navigationHolder::restoreState)
        // Back stack может оказаться пустым, если состояние не удалось восстановить
        if (savedInstanceState == null || router.backStack.isEmpty()) {
            launchFirstScreen()
        }
        findViewById<ComposeView>(R.id.composeRoot).setContent {
            App(router = router, navEntries = navEntries)
        }

        deeplinkHandlersProcessor.processDeeplink(intent.data)
    }

    private fun launchFirstScreen() {
        val selectedGroup = sharedPreferences.getString("selected_group", "")
        if (!selectedGroup.isNullOrEmpty()) {
            FirebaseCrashlytics.getInstance().setCustomKey("schedule_name", selectedGroup)
            mainScreenLauncher.launch()
        } else {
            onboardingFeatureApi.launch()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(KEY_NAVIGATION_STATE, navigationHolder.saveState())
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        deeplinkHandlersProcessor.processDeeplink(intent.data)
    }

    private fun setTheme() {
        val preheatAppSettings = appSettingsRepository.getAppSettings()
        val darkTheme = preheatAppSettings.isDarkThemeEnabled
        if (darkTheme) {
            setTheme(coreui_R.style.AppTheme_Dark)
        } else {
            setTheme(coreui_R.style.AppTheme)
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
                detectDarkMode = { darkTheme },
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
                detectDarkMode = { darkTheme },
            ),
        )
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.subscribe(this)
    }

    override fun onPause() {
        navigationHolder.unsubscribe()
        super.onPause()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleContextWrapper.wrapContext(newBase))
    }

    private companion object {
        const val KEY_NAVIGATION_STATE = "MainActivity.NavigationState"
    }
}
