package kekmech.ru.ui_kit_switch.embedded

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Indicator of a boolean state.
 *
 * Unlike Material Switch,
 * [EmbeddedSwitch] is not a standalone component and must not be clickable.
 * It is used only as a part of a list item or another layout component.
 */
@Suppress("AnimateAsStateLabel")
@Composable
public fun EmbeddedSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    palette: EmbeddedSwitchPalette = EmbeddedSwitchPalette.Default,
    form: EmbeddedSwitchForm = EmbeddedSwitchForm.M,
) {
    // Colors use the same animation spec as the thumb, so the whole switch changes in sync
    val trackColor by animateColorAsState(
        targetValue = trackColor(palette, enabled, checked),
        animationSpec = tween(ANIMATION_DURATION_MILLIS),
    )
    val borderColor by animateColorAsState(
        targetValue = borderColor(palette, enabled, checked),
        animationSpec = tween(ANIMATION_DURATION_MILLIS),
    )
    val thumbColor by animateColorAsState(
        targetValue = thumbColor(palette, enabled, checked),
        animationSpec = tween(ANIMATION_DURATION_MILLIS),
    )

    val thumbProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(ANIMATION_DURATION_MILLIS),
    )

    Canvas(
        modifier = modifier
            .wrapContentSize(Alignment.Center)
            .requiredSize(form.width, form.height)
            .semantics {
                this.role = Role.Switch
                this.toggleableState = ToggleableState(checked)
            },
    ) {
        val cornerRadius = size.height / 2f

        val checkedThumbDiameter = (form.height - THUMB_VERTICAL_PADDING * 2).toPx()
        val uncheckedThumbDiameter = checkedThumbDiameter * UNCHECKED_THUMB_SCALE
        val thumbDiameter =
            uncheckedThumbDiameter + (checkedThumbDiameter - uncheckedThumbDiameter) * thumbProgress
        val thumbRadius = thumbDiameter / 2f

        val thumbPadding = (size.height - thumbDiameter) / 2f
        val uncheckedCenterX = thumbPadding + thumbRadius
        val checkedCenterX = size.width - thumbPadding - thumbRadius
        val thumbCenterX = uncheckedCenterX + (checkedCenterX - uncheckedCenterX) * thumbProgress

        drawRoundRect(
            color = trackColor,
            cornerRadius = CornerRadius(cornerRadius),
            size = Size(size.width, size.height),
        )

        drawRoundRect(
            color = borderColor,
            cornerRadius = CornerRadius(cornerRadius),
            size = Size(size.width, size.height),
            style = Stroke(width = STROKE_WIDTH.toPx()),
        )

        drawCircle(
            color = thumbColor,
            radius = thumbRadius,
            center = Offset(
                x = thumbCenterX,
                y = size.height / 2f,
            ),
        )
    }
}

private const val ANIMATION_DURATION_MILLIS = 300
private const val UNCHECKED_THUMB_SCALE = 0.67f
private val STROKE_WIDTH = 2.dp
private val THUMB_VERTICAL_PADDING = 4.dp

// Hidden parts are drawn with the same color as in the visible state, but fully transparent.
// Color.Transparent is transparent black, so animating to/from it would darken the color midway.

@Composable
private fun trackColor(palette: EmbeddedSwitchPalette, enabled: Boolean, checked: Boolean): Color =
    when {
        !enabled && checked -> palette.disabledChecked
        !enabled -> palette.disabledChecked.copy(alpha = 0f)
        checked -> palette.checked
        else -> palette.checked.copy(alpha = 0f)
    }

@Composable
private fun borderColor(palette: EmbeddedSwitchPalette, enabled: Boolean, checked: Boolean): Color =
    when {
        !enabled && checked -> palette.disabled.copy(alpha = 0f)
        !enabled -> palette.disabled
        checked -> palette.borderChecked
        else -> palette.borderUnchecked
    }

@Composable
private fun thumbColor(palette: EmbeddedSwitchPalette, enabled: Boolean, checked: Boolean): Color =
    when {
        !enabled && checked -> palette.icon
        !enabled -> palette.disabled
        checked -> palette.icon
        else -> palette.unchecked
    }

@PreviewLightDark
@Composable
private fun Preview() {
    MpeixTheme {
        Row(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column {
                EmbeddedSwitch(false, form = EmbeddedSwitchForm.S)
                Spacer(Modifier.height(8.dp))
                EmbeddedSwitch(true, form = EmbeddedSwitchForm.S)
                Spacer(Modifier.height(8.dp))
                EmbeddedSwitch(false, enabled = false, form = EmbeddedSwitchForm.S)
                Spacer(Modifier.height(8.dp))
                EmbeddedSwitch(true, enabled = false, form = EmbeddedSwitchForm.S)
            }
            Column {
                EmbeddedSwitch(false, form = EmbeddedSwitchForm.M)
                Spacer(Modifier.height(8.dp))
                EmbeddedSwitch(true, form = EmbeddedSwitchForm.M)
                Spacer(Modifier.height(8.dp))
                EmbeddedSwitch(false, enabled = false, form = EmbeddedSwitchForm.M)
                Spacer(Modifier.height(8.dp))
                EmbeddedSwitch(true, enabled = false, form = EmbeddedSwitchForm.M)
            }
        }
    }
}
