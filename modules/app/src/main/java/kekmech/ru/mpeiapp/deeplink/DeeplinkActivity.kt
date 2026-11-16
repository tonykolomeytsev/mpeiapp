package kekmech.ru.mpeiapp.deeplink

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import kekmech.ru.mpeiapp.MainActivity

class DeeplinkActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deeplink = intent.data
        startActivity(Intent(this, MainActivity::class.java).apply { data = deeplink })
        finish()
    }
}
