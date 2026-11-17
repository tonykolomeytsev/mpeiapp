package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_schedule_impl.R
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

private const val FAB_ANIMATION_DURATION_MS = 100
private val FabHeight = 48.dp

/**
 * # ScheduleNavigationFab
 *
 * Quick navigation to the next week, or back to the current one from any other week.
 * Long press shows the tooltip about turning the button off in the settings.
 *
 * @param isNextWeek true if the button leads to the next week, false if back to the current one
 * @param isVisible the button fades out when false and doesn't react to clicks while hidden
 * @param onClick called when the visible button is clicked
 * @param modifier [Modifier] to be applied to the button
 */
@Composable
internal fun ScheduleNavigationFab(
    isNextWeek: Boolean,
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(FAB_ANIMATION_DURATION_MS),
        label = "ScheduleNavigationFabAlpha",
    )
    val text = stringResource(if (isNextWeek) Strings.schedule_fab_next else Strings.schedule_fab_prev)
    val icon = if (isNextWeek) R.drawable.ic_next_schedule else R.drawable.ic_prev_schedule
    // TooltipBox wraps the anchor into its own Box and applies the modifier to the anchor inside it,
    // so parent data modifiers (e.g. align) of the caller wouldn't reach the caller's layout
    Box(modifier = modifier) {
        ScheduleNavigationFabContent(
            text = text,
            icon = icon,
            isVisible = isVisible,
            alpha = alpha,
            onClick = onClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleNavigationFabContent(
    text: String,
    @DrawableRes icon: Int,
    isVisible: Boolean,
    alpha: Float,
    onClick: () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(stringResource(Strings.schedule_fab_tooltip)) } },
        state = rememberTooltipState(),
        enableUserInput = isVisible,
    ) {
        ExtendedFloatingActionButton(
            text = {
                Text(
                    text = text.uppercase(),
                    style = MpeixTheme.typography.titleSmall,
                    maxLines = 1,
                )
            },
            icon = { Icon(painter = painterResource(icon), contentDescription = null) },
            // The hidden button stays in the layout like the View with zero alpha did,
            // so it keeps its place and just ignores the clicks
            onClick = { if (isVisible) onClick() },
            modifier = Modifier
                .height(FabHeight)
                .alpha(alpha)
                .then(if (isVisible) Modifier else Modifier.semantics { hideFromAccessibility() }),
            containerColor = MpeixTheme.palette.primary,
            contentColor = MpeixTheme.palette.onPrimary,
            shape = CircleShape,
        )
    }
}

@PreviewLightDark
@Composable
private fun ScheduleNavigationFabPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
        ) {
            ScheduleNavigationFab(isNextWeek = true, isVisible = true, onClick = {})
            ScheduleNavigationFab(
                isNextWeek = false,
                isVisible = true,
                onClick = {},
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}
