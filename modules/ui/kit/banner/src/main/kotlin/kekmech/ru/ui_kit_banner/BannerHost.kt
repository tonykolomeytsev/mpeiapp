package kekmech.ru.ui_kit_banner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.delay

private const val ANIMATION_DURATION_MILLIS = 200
private const val BANNER_SHOWING_DURATION_MILLIS = 3000L

/**
 * # BannerHost
 *
 * Compose version of the `BannerContainer` from coreui, but local to the screen.
 * Shows the [BannerHostState.currentBanner] at the top of the screen, under the status bar,
 * and hides it after 3 seconds.
 *
 * Place it above the screen content, e.g. in the [androidx.compose.foundation.layout.Box]
 * together with the `Scaffold`.
 *
 * Usage example:
 * ```kotlin
 * val bannerHostState = remember { BannerHostState() }
 * Box {
 *     Scaffold { ... }
 *     BannerHost(bannerHostState)
 * }
 * ```
 *
 * @param state the state of the host
 * @param modifier [Modifier] to be applied to the banner
 */
@Composable
public fun BannerHost(
    state: BannerHostState,
    modifier: Modifier = Modifier,
) {
    val currentBanner = state.currentBanner
    // Keeps the text of the hidden banner, while it is fading out
    var lastMessage by remember { mutableStateOf("") }
    if (currentBanner != null) {
        lastMessage = currentBanner.message
    }
    LaunchedEffect(currentBanner) {
        if (currentBanner != null) {
            delay(BANNER_SHOWING_DURATION_MILLIS)
            state.dismiss(currentBanner)
        }
    }
    AnimatedVisibility(
        visible = currentBanner != null,
        modifier = modifier,
        enter = fadeIn(tween(ANIMATION_DURATION_MILLIS)),
        exit = fadeOut(tween(ANIMATION_DURATION_MILLIS)),
    ) {
        Banner(message = lastMessage)
    }
}

@Composable
private fun Banner(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.error)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 32.dp, vertical = 16.dp),
    ) {
        Text(
            text = message,
            style = MpeixTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = MpeixTheme.palette.onError,
        )
    }
}

@PreviewLightDark
@Composable
private fun BannerPreview() {
    MpeixTheme {
        Banner(message = "Произошла ошибка при загрузке данных с сервера")
    }
}
