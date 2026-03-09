package kekmech.ru.ui_kit_buttons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.ui_theme.typography.RobotoFontFamily
import kotlinx.coroutines.delay

private val LegacyH4 = TextStyle(
    fontFamily = RobotoFontFamily,
    lineHeight = 22.sp,
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
)

@Composable
public fun LegacyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    loading: Boolean = false,
) {
    Button(
        onClick = {
            if (enabled && !loading) {
                onClick.invoke()
            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .heightIn(min = 48.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = remember { text.uppercase() },
                style = LegacyH4,
                modifier = Modifier.alpha(if (loading) 0f else 1f),
            )
            if (loading) {
                ThreeBounceAnimation()
            }
        }
    }
}

/**
 * Copy-pasted from https://github.com/canopas/compose-animations-examples
 */
@Composable
private fun ThreeBounceAnimation() {
    val dots = listOf(
        remember { Animatable(0.0f) },
        remember { Animatable(0.0f) },
        remember { Animatable(0.0f) },
    )

    dots.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 200L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Reverse,
                )
            )
        }
    }

    val dys = dots.map { it.value }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        dys.fastForEach { dy ->
            Box(
                Modifier
                    .size(8.dp)
                    .alpha(dy)
                    .background(color = Color.White, shape = CircleShape)
            )
        }
    }
}

@Preview
@Composable
private fun LegacyButtonPreview() {
    MpeixTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            LegacyButton(
                text = "Continue",
                onClick = { /* no-op */ },
            )
            LegacyButton(
                text = "Continue",
                onClick = { /* no-op */ },
                loading = true,
            )
            LegacyButton(
                text = "Continue",
                onClick = { /* no-op */ },
                enabled = false,
            )
        }
    }
}