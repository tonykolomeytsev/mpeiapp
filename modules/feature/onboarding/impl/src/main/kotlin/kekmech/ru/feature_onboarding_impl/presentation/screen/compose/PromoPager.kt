package kekmech.ru.feature_onboarding_impl.presentation.screen.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.fastCoerceAtLeast
import kekmech.ru.feature_onboarding_impl.presentation.item.PromoPage
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlin.math.abs

private val PagerMaxHeight = 400.dp
private val IllustrationMaxSize = 250.dp

@Composable
internal fun PromoPager(
    modifier: Modifier = Modifier,
) {
    val pages = PromoPage.entries
    val pagerState = rememberPagerState { pages.size }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Takes all the height it is given, but not more than PagerMaxHeight,
        // so the pager shrinks on short screens (landscape, split screen)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f, fill = false)
                .heightIn(max = PagerMaxHeight)
                .fillMaxHeight(),
        ) { index ->
            PromoPageContent(pages[index])
        }
        PagerDots(
            pagerState = pagerState,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun PromoPageContent(
    page: PromoPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Texts are measured first, the illustration gets the remaining height
        Image(
            painter = painterResource(page.icon),
            contentDescription = null,
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(bottom = 24.dp)
                .sizeIn(maxWidth = IllustrationMaxSize, maxHeight = IllustrationMaxSize)
                .aspectRatio(1f),
        )
        Text(
            text = stringResource(page.title),
            style = MpeixTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        )
        Text(
            text = stringResource(page.subtitle),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
    }
}

@Composable
private fun PagerDots(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotColor: Color = MpeixTheme.palette.outline,
    selectedDotColor: Color = MpeixTheme.palette.primary,
    dotSize: Dp = 6.dp,
    selectedDotSize: Dp = 10.dp,
    dotSpacing: Dp = 8.dp,
) {
    val step = dotSize + dotSpacing
    Canvas(
        modifier = modifier
            .size(
                width = step * (pagerState.pageCount - 1) + selectedDotSize,
                height = selectedDotSize,
            )
    ) {
        val position = pagerState.currentPage + pagerState.currentPageOffsetFraction
        repeat(pagerState.pageCount) { i ->
            val fraction = (1f - abs(i - position)).fastCoerceAtLeast(0f)
            drawCircle(
                color = lerp(dotColor, selectedDotColor, fraction),
                radius = lerp(dotSize, selectedDotSize, fraction).toPx() / 2,
                center = Offset(selectedDotSize.toPx() / 2 + step.toPx() * i, center.y),
            )
        }
    }
}

@Composable
@Preview
private fun PromoPagerPreview() {
    MpeixTheme {
        PromoPager()
    }
}