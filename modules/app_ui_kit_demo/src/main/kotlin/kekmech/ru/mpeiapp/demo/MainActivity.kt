package kekmech.ru.mpeiapp.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import kekmech.ru.common_compose_theme.theme.MpeixTheme

class MainActivity : NodeComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MpeixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MpeixTheme.palette.background,
                ) {
                    NodeHost(integrationPoint = appyxIntegrationPoint) {
                        BackStackRootNode(buildContext = it)
                    }
                }
            }
        }
    }
}