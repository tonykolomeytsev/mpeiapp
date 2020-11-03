package kekmech.ru.mpeiapp.deeplink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kekmech.ru.mpeiapp.MainActivity

class DeeplinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deeplink = intent.data
        startActivity(Intent(this, MainActivity::class.java).apply { data = deeplink })
        finish()
    }
}