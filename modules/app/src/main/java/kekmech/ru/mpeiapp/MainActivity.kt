package kekmech.ru.mpeiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

class MainActivity : ComponentActivity() {

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
        super.onCreate(savedInstanceState)
        mainActivityLifecycleObservers.forEach { it.onCreate(this) }

        // AppTheme itself is applied by the splash screen (postSplashScreenTheme)
        setUpSystemBars()

        savedInstanceState?.getBundle(KEY_NAVIGATION_STATE)?.let(navigationHolder::restoreState)
        // Back stack может оказаться пустым, если состояние не удалось восстановить
        if (savedInstanceState == null || router.backStack.isEmpty()) {
            launchFirstScreen()
        }
        setContent {
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

    private fun setUpSystemBars() {
        val darkTheme = appSettingsRepository.getAppSettings().isDarkThemeEnabled
        // Explicit light or dark style instead of SystemBarStyle.auto: with auto the system draws
        // its own translucent scrim behind the 3-button navigation bar. Changing the theme
        // recreates the activity, so there is nothing to detect at runtime
        val style = if (darkTheme) {
            SystemBarStyle.dark(Color.TRANSPARENT)
        } else {
            SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        }
        enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
    }

    override fun onResume() {
        super.onResume()
        navigationHolder.subscribe(this)
    }

    override fun onPause() {
        navigationHolder.unsubscribe()
        super.onPause()
    }

    override fun attachBaseContext(newBase: Context) {
        // Applied before the activity creates its resources, so the window theme, XML resources
        // and Compose start with the app's dark theme setting. Changing the setting recreates the activity
        val isDarkTheme = appSettingsRepository.getAppSettings().isDarkThemeEnabled
        val localizedContext = LocaleContextWrapper.wrapContext(newBase)
        super.attachBaseContext(NightModeContextWrapper.wrapContext(localizedContext, isDarkTheme))
    }

    private companion object {
        const val KEY_NAVIGATION_STATE = "MainActivity.NavigationState"
    }
}
