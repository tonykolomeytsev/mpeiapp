package kekmech.ru.mpeiapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kekmech.ru.mpeiapp.ComposeEnabledFeatureToggle
import kekmech.ru.mpeiapp.ComposeMainActivity
import kekmech.ru.mpeiapp.MainActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val composeEnabledFeatureToggle: ComposeEnabledFeatureToggle by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (composeEnabledFeatureToggle.value) {
            startActivity(Intent(this, ComposeMainActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
