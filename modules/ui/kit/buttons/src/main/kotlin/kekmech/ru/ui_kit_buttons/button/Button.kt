package kekmech.ru.ui_kit_buttons.button

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
@NonRestartableComposable
public fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    palette: ButtonPalette = ButtonPalette.Primary,
    form: ButtonForm = ButtonForm.Default,
    state: ButtonState = ButtonState.Default,
) {
    androidx.compose.material3.Button(
        onClick = {
            if (state == ButtonState.Default) {
                onClick.invoke()
            }
        },
        modifier = modifier
            .sizeIn(
                minWidth = form.minWidth,
                minHeight = form.minHeight,
            ),
        enabled = true,
        shape = form.shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = palette.background,
            contentColor = palette.text,
        ),
        contentPadding = PaddingValues(
            horizontal = form.internalHorizontalPadding,
            vertical = form.internalVerticalPadding,
        ),
        content = {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = remember { text.uppercase() },
                    style = form.textStyle,
                    modifier = Modifier.alpha(if (state == ButtonState.Loading) 0f else 1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (state == ButtonState.Loading) {
                    PulsatingDot(
                        dotSize = form.progressDotSize,
                        dotSpacing = form.progressDotSpacing,
                    )
                }
            }
        },
    )
}

@Composable
private fun PulsatingDot(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    dotSize: Dp = 8.dp,
    dotSpacing: Dp = 16.dp,
) {
    val transition = rememberInfiniteTransition(label = "PulsatingDots")
    val scales = List(3) { i ->
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 600, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(offsetMillis = 200 * i),
            ),
            label = "dot$i"
        )
    }
    Canvas(
        modifier = modifier
            .progressSemantics()
            .size(
                width = dotSize * 3 + dotSpacing * 2,
                height = dotSpacing,
            ),
    ) {
        val radius = dotSize.toPx() / 2f
        val step = (dotSize + dotSpacing).toPx()
        scales.fastForEachIndexed { i, scale ->
            drawCircle(
                color = color,
                radius = radius * scale.value,
                center = center + Offset(step * (i - 1), 0f),
            )
        }
    }
}

@Composable
@Preview
private fun ButtonPrimaryPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                text = "Primary Button (MAX WIDTH)",
                onClick = { /* no-op */ },
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                text = "Primary Button",
                onClick = { /* no-op */ },
            )
            Button(
                text = "Primary Button (LOADING)",
                onClick = { /* no-op */ },
                modifier = Modifier.fillMaxWidth(),
                state = ButtonState.Loading,
            )
        }
    }
}

@Composable
@Preview
private fun ButtonSecondaryPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                text = "Primary Button (MAX WIDTH)",
                onClick = { /* no-op */ },
                modifier = Modifier.fillMaxWidth(),
                palette = ButtonPalette.Secondary,
            )
            Button(
                text = "Primary Button",
                onClick = { /* no-op */ },
                palette = ButtonPalette.Secondary,
            )
            Button(
                text = "Primary Button (LOADING)",
                onClick = { /* no-op */ },
                modifier = Modifier.fillMaxWidth(),
                state = ButtonState.Loading,
                palette = ButtonPalette.Secondary,
            )
        }
    }
}
