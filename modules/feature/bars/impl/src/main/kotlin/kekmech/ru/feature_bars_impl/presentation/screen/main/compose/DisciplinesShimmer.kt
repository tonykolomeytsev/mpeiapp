package kekmech.ru.feature_bars_impl.presentation.screen.main.compose

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val SHIMMER_DISCIPLINES_COUNT = 4
private val LineShape = RoundedCornerShape(8.dp)
private val CardShape = RoundedCornerShape(12.dp)

/** Placeholder of the section header and the [DisciplineItem]s while the marks are loading. */
@Composable
internal fun DisciplinesShimmer(modifier: Modifier = Modifier) {
    val lineColor = MpeixTheme.palette.outlineVariant
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shimmer(),
    ) {
        // Same place as the title and subtitle of the SectionHeader
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        ) {
            Line(width = 140.dp, height = 22.dp)
            Line(width = 200.dp, height = 16.dp)
        }
        repeat(SHIMMER_DISCIPLINES_COUNT) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MpeixTheme.palette.surfaceContainer, CardShape)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
            ) {
                // Name, teacher with assessment type, then the mark
                Box(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp)
                        .background(lineColor, LineShape)
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .background(lineColor, LineShape)
                )
                Line(width = 56.dp, height = 22.dp)
            }
        }
    }
}

@Composable
private fun Line(width: Dp, height: Dp) {
    Box(
        Modifier
            .width(width)
            .height(height)
            .background(MpeixTheme.palette.outlineVariant, LineShape)
    )
}

@PreviewLightDark
@Composable
private fun DisciplinesShimmerPreview() {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            DisciplinesShimmer()
        }
    }
}
