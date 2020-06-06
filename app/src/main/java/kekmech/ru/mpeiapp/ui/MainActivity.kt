package kekmech.ru.mpeiapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.analytics.FirebaseAnalytics
import kekmech.ru.common_android.onActivityResult
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.common_navigation.NavigationHolder
import kekmech.ru.core.usecases.IsDarkThemeEnabledUseCase
import kekmech.ru.mpeiapp.R
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigationHolder: NavigationHolder by inject()

    val isDarkThemeEnabledUseCase: IsDarkThemeEnabledUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            MapsInitializer.initialize(this)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
        FirebaseAnalytics.getInstance(this)

        if (isDarkThemeEnabledUseCase()) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }

        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        navigationHolder.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        navigationHolder.unsubscribe()
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
