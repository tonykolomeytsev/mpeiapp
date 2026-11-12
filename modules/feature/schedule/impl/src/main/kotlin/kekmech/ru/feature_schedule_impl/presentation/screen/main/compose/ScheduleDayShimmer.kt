package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val SHIMMER_CLASSES_COUNT = 3
private val CardShape = RoundedCornerShape(12.dp)
private val LineShape = RoundedCornerShape(8.dp)

/**
 * Placeholder of the day classes while the schedule is loading
 */
@Composable
internal fun ScheduleDayShimmer(modifier: Modifier = Modifier) {
    val cardColor = MpeixTheme.palette.secondary
    val lineColor = MpeixTheme.palette.outlineVariant
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(SHIMMER_CLASSES_COUNT) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(cardColor, CardShape)
                    .padding(start = 80.dp, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    Modifier
                        .padding(end = 32.dp)
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(lineColor, LineShape)
                )
                Box(
                    Modifier
                        .padding(end = 150.dp)
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(lineColor, LineShape)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ScheduleDayShimmerPreview() {
    MpeixTheme {
        ScheduleDayShimmer(
            Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp)
        )
    }
}
