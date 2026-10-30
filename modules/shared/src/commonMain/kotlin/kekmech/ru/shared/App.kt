package kekmech.ru.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * The root of the shared UI. Milestone 0 of the iOS port: it only proves the whole pipeline
 * (Kotlin/Native -> framework -> Xcode -> .ipa). The real screens move here module by module.
 */
@Composable
public fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize().safeContentPadding(),
            contentAlignment = Alignment.Center,
        ) {
            Text("МЭИ.Хаб · iOS")
        }
    }
}
