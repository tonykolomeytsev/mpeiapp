package kekmech.ru.mpeiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.ext_android.onActivityResult
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityLifecycleObservers.forEach { it.onCreate(this) }

        setTheme()
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

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
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        deeplinkHandlersProcessor.processDeeplink(intent.data)
    }

    private fun setTheme() {
        val preheatAppSettings = appSettingsRepository.getAppSettings()
        if (preheatAppSettings.isDarkThemeEnabled) {
            setTheme(coreui_R.style.AppTheme_Dark)
        } else {
            setTheme(coreui_R.style.AppTheme)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.subscribe(this)
    }

    override fun onPause() {
        navigationHolder.unsubscribe()
        super.onPause()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleContextWrapper.wrapContext(newBase))
    }
}
