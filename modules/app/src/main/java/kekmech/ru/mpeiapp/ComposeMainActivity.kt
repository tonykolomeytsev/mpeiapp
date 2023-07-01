package kekmech.ru.mpeiapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import kekmech.ru.common_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.getKoin

class ComposeMainActivity : NodeComponentActivity() {

    private val mainActivityLifecycleObservers: List<MainActivityLifecycleObserver> by lazy {
        getKoin().getAll()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        mainActivityLifecycleObservers.forEach { it.onCreate(this) }
        if (Build.VERSION.SDK_INT < 25) LocaleContextWrapper.updateResourcesV24(this)

        setContent {
            MpeixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MpeixTheme.palette.background,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "Hello, Compose!", style = MpeixTheme.typography.header2)
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleContextWrapper.wrapContext(newBase))
    }
}
