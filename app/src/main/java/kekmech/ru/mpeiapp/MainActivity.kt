package kekmech.ru.mpeiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.analytics.FirebaseAnalytics
import kekmech.ru.common_android.onActivityResult
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.common_navigation.NavigationHolder
import kekmech.ru.common_navigation.NewRoot
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.mpeiapp.ui.main.MainFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigationHolder: NavigationHolder by inject()
    private val router: Router by inject()
    private val appSettings: AppSettings by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            MapsInitializer.initialize(this)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
        FirebaseAnalytics.getInstance(this)

        setTheme()
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        enableEdgeToEdge()


        if (savedInstanceState == null) {
            router.executeCommand(NewRoot { MainFragment.newInstance() })
            //router.executeCommand(NewRoot { WelcomeFragment() })
        }
    }

    private fun enableEdgeToEdge() {
        window.decorView.apply {
            systemUiVisibility = systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }

    private fun setTheme() {
        if (appSettings.isDarkThemeEnabled) {
            setTheme(R.style.AppTheme_Dark)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                window.decorView.apply {
//                    systemUiVisibility = systemUiVisibility xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                }
        } else {
            setTheme(R.style.AppTheme)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                window.decorView.apply {
//                    systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                }
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
}
