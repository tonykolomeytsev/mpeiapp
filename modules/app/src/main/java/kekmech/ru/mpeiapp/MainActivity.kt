package kekmech.ru.mpeiapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_main_screen_api.MainScreenLauncher
import kekmech.ru.feature_onboarding_api.OnboardingFeatureApi
import kekmech.ru.lib_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.lib_navigation.NavigationHolder
import kekmech.ru.lib_navigation.NewRoot
import kekmech.ru.lib_navigation.Router
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandlersProcessor
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
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !isReady || (System.currentTimeMillis() - startTime < MIN_SPLASH_SCREEN_DURATION_MS)
        }
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val splashScreenView = splashScreenViewProvider.view
            val alphaAnimator = ObjectAnimator.ofFloat(
                splashScreenView,
                View.ALPHA,
                1f,
                0f,
            )
            val scaleXAnimator = ObjectAnimator.ofFloat(
                splashScreenView,
                View.SCALE_X,
                1f,
                SPLASH_SCREEN_EXIT_SCALE,
            )
            val scaleYAnimator = ObjectAnimator.ofFloat(
                splashScreenView,
                View.SCALE_Y,
                1f,
                SPLASH_SCREEN_EXIT_SCALE,
            )

            AnimatorSet().apply {
                duration = SPLASH_SCREEN_EXIT_DURATION_MS
                interpolator = AccelerateInterpolator()
                playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator)
                doOnEnd {
                    splashScreenViewProvider.remove()
                }
                start()
            }
        }

        mainActivityLifecycleObservers.forEach { it.onCreate(this) }

        setTheme()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val selectedGroup = sharedPreferences.getString("selected_group", "")
            if (!selectedGroup.isNullOrEmpty()) {
                FirebaseCrashlytics.getInstance().setCustomKey("schedule_name", selectedGroup)
                mainScreenLauncher.launch()
            } else {
                router.executeCommand(NewRoot { onboardingFeatureApi.getScreen() })
            }
        }

        deeplinkHandlersProcessor.processDeeplink(intent.data)
        isReady = true
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

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        setTheme()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleContextWrapper.wrapContext(newBase))
    }

    private companion object {

        const val MIN_SPLASH_SCREEN_DURATION_MS = 1350L
        const val SPLASH_SCREEN_EXIT_DURATION_MS = 250L
        const val SPLASH_SCREEN_EXIT_SCALE = 1.05f
    }
}
