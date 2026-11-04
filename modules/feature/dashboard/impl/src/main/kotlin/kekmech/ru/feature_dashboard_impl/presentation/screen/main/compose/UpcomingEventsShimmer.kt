package kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val SHIMMER_CLASSES_COUNT = 2

/**
 * Placeholder of the upcoming events while they are loading.
 */
@Composable
internal fun UpcomingEventsShimmer(modifier: Modifier = Modifier) {
    val color = MpeixTheme.palette.outlineVariant
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shimmer(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            Modifier
                .padding(top = 2.dp, bottom = 4.dp)
                .width(150.dp)
                .height(18.dp)
                .background(color, RoundedCornerShape(8.dp))
        )
        repeat(SHIMMER_CLASSES_COUNT) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color, RoundedCornerShape(12.dp))
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun UpcomingEventsShimmerPreview() {
    MpeixTheme {
        UpcomingEventsShimmer(Modifier.background(MpeixTheme.palette.surface))
    }
}
