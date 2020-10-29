package kekmech.ru.mpeiapp.deeplink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kekmech.ru.mpeiapp.MainActivity
import org.koin.android.ext.android.inject

class DeeplinkActivity : AppCompatActivity() {

    private val deeplinkHandlersProcessor by inject<DeeplinkHandlersProcessor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deeplink = intent.data
        startActivity(Intent(this, MainActivity::class.java))
        deeplinkHandlersProcessor.processDeeplink(deeplink)
        finish()
    }
}