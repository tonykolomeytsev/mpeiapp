package kekmech.ru.mpeiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.common_android.onActivityResult
import kekmech.ru.common_mvi.DisposableDelegate
import kekmech.ru.common_mvi.DisposableDelegateImpl
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.common_navigation.NavigationHolder
import kekmech.ru.common_network.device_id.DeviceIdProvider
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_main_screen.MainScreenLauncher
import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandlersProcessor
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), DisposableDelegate by DisposableDelegateImpl() {

    private val navigationHolder: NavigationHolder by inject()
    private val appSettings: AppSettings by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val onboardingFeatureLauncher: OnboardingFeatureLauncher by inject()
    private val mainScreenLauncher: MainScreenLauncher by inject()
    private val deeplinkHandlersProcessor: DeeplinkHandlersProcessor by inject()
    private val deviceIdProvider: DeviceIdProvider by inject()

    @Suppress("MagicNumber", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            MapsInitializer.initialize(this)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
        FirebaseAnalytics.getInstance(this).apply {
            setUserId(deviceIdProvider.getDeviceId())
        }

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
        if (Build.VERSION.SDK_INT < 25) LocaleContextWrapper.updateResourcesV24(this)
    }

    @Suppress("DEPRECATION") // deprecated с андроида 11, но на 11 работает норм
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
        if (appSettings.isDarkThemeEnabled) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
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
