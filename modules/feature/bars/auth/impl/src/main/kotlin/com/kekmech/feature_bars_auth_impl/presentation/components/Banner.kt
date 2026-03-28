package com.kekmech.feature_bars_auth_impl.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kekmech.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.delay

internal class BannerState {
    val offset = Animatable(-1f)
    var message by mutableStateOf("")
        private set

    suspend fun show(text: String) {
        message = text
        offset.animateTo(0f, animationSpec = tween(500))
        delay(3000)
        offset.animateTo(-1f, animationSpec = tween(500))
    }
}

@Composable
internal fun rememberBannerState() = remember { BannerState() }

@Composable
internal fun Banner(
    state: BannerState,
    modifier: Modifier = Modifier,
) {
    if (state.offset.value > -1f) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, (state.offset.value * 300).coerceAtMost(0f).toInt()) }
                    .graphicsLayer { alpha = 1f + state.offset.value },
            ) {
                Text(
                    text = state.message,
                    color = MpeixTheme.palette.contentAccent,
                    style = MpeixTheme.typography.labelBig,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color(0xFFDB4437))
                        .padding(vertical = 16.dp, horizontal = 32.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun BannerPreview() {
    MpeixPreview {
        val bannerState = rememberBannerState()
        LaunchedEffect(Unit) {
            bannerState.show("Network error")
        }
        Banner(
            state = bannerState,
        )
    }
}