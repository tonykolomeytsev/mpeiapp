package kekmech.ru.feature_debug_menu.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import kekmech.ru.ui_theme.theme.MpeixTheme

internal class DebugMenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MpeixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MpeixTheme.palette.background
                ) {
                    Text("Debug menu!", style = MpeixTheme.typography.paragraphBig, color = MpeixTheme.palette.content)
                }
            }
        }
    }
}
