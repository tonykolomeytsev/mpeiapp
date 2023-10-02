package kekmech.ru.ui_shimmer

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal object ShimmerTokens {

    const val Rotation: Float = 15f // deg
    val Width: Dp = 400.dp
    val AnimationSpec: AnimationSpec<Float> = infiniteRepeatable(
        animation = tween(
            durationMillis = 700,
            easing = LinearEasing,
            delayMillis = 500,
        ),
        repeatMode = RepeatMode.Restart,
    )
}
