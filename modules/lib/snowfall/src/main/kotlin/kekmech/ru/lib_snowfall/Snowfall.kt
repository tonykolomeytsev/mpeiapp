package kekmech.ru.lib_snowfall

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

/**
 * Draws the snow of [state] over its bounds. Touches go through to the content below.
 *
 * The snow moves only while some [Snowfall] with this [state] is in the composition.
 */
@Composable
public fun Snowfall(
    state: SnowfallState,
    color: Color,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(state) { state.animate() }
    // Own layer: every step re-records only the snow, not the content under it
    Canvas(modifier.graphicsLayer()) {
        with(state) { drawSnow(color) }
    }
}

/**
 * Flakes start above the top edge, run Interactive Mode to see them fall.
 */
@Preview
@Composable
private fun SnowfallPreview() {
    Snowfall(
        state = rememberSnowfallState(),
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E)),
    )
}
