package kekmech.ru.mpeiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.coreui.banner.findBanner
import kekmech.ru.domain_main_screen.MainScreenLauncher
import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.ext_android.onActivityResult
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.library_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.library_elm.DisposableDelegate
import kekmech.ru.library_elm.DisposableDelegateImpl
import kekmech.ru.library_navigation.BackButtonListener
import kekmech.ru.library_navigation.NavigationHolder
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandlersProcessor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import kekmech.ru.coreui.R as coreui_R
import kekmech.ru.library_navigation.R as common_navigation_R

class MainActivity : AppCompatActivity(), DisposableDelegate by DisposableDelegateImpl() {

    private val navigationHolder: NavigationHolder by inject()
    private val appSettingsRepository: AppSettingsRepository by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val onboardingFeatureLauncher: OnboardingFeatureLauncher by inject()
    private val mainScreenLauncher: MainScreenLauncher by inject()
    private val deeplinkHandlersProcessor: DeeplinkHandlersProcessor by inject()
    private val mainActivityLifecycleObservers: List<MainActivityLifecycleObserver> by lazy {
        getKoin().getAll()
    }

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
                onboardingFeatureLauncher.launchWelcomePage(true)
            }
        }

        deeplinkHandlersProcessor.processDeeplink(intent.data)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            LocaleContextWrapper.updateResourcesV24(this)
        }
    }

    @Suppress("DEPRECATION", "UnnecessaryApply")
    private fun enableEdgeToEdge() {
        window.decorView.apply {
            systemUiVisibility = systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        deeplinkHandlersProcessor.processDeeplink(intent?.data)
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

    override fun onStop() {
        findBanner()?.dispose()
        dispose()
        super.onStop()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.onActivityResult(requestCode, resultCode, data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(common_navigation_R.id.container)
        if (fragment != null && fragment is BackButtonListener && fragment.onBackPressed()) {
            return
        } else {
            super.onBackPressed()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleContextWrapper.wrapContext(newBase))
    }
}
