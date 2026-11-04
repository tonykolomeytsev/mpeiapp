package kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val ANIMATION_DURATION_MILLIS = 200

/**
 * Current date and status of the week at the top of the dashboard.
 *
 * @param header formatted current date
 * @param description status of the week, or null while it is loading
 */
@Composable
internal fun DayStatus(
    header: String,
    description: String?,
    modifier: Modifier = Modifier,
) {
    val descriptionAlpha by animateFloatAsState(
        targetValue = if (description == null) 0f else 1f,
        animationSpec = tween(ANIMATION_DURATION_MILLIS),
        label = "DayStatusDescription",
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = header,
            modifier = Modifier.padding(top = 4.dp), // fine-tuning pixel perfect old view
            style = MpeixTheme.typography.headlineSmall,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            // Space keeps the height of the line while the description is loading
            text = description ?: " ",
            modifier = Modifier
                .padding(top = 6.dp) // also fine-tuning, was 4.dp
                .alpha(descriptionAlpha),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

@PreviewLightDark
@Composable
private fun DayStatusPreview() {
    MpeixTheme {
        DayStatus(
            header = "Вторник, 7 мая",
            description = "Идет 12 учебная неделя",
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}
