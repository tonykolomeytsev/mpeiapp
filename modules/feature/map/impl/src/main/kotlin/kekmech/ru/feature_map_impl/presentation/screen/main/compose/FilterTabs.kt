package kekmech.ru.feature_map_impl.presentation.screen.main.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_map_impl.R
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.FilterTab
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun FilterTabs(
    onTabClick: (FilterTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp),
    ) {
        items(FilterTab.entries, key = { it }) { tab ->
            FilterTabItem(
                iconRes = tab.iconRes,
                nameRes = tab.nameRes,
                onClick = { onTabClick(tab) },
            )
        }
    }
}

@Composable
private fun FilterTabItem(
    @DrawableRes iconRes: Int,
    @StringRes nameRes: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MpeixTheme.palette.surfaceContainer)
                .clickable(onClick = onClick),
        ) {
            // Icons are drawn with an offset, so their bottom right corner is cut by the card
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 6.dp, top = 6.dp)
                    .size(56.dp),
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(nameRes),
            style = MpeixTheme.typography.bodySmall,
            color = MpeixTheme.palette.onSurface,
        )
    }
}

private val FilterTab.iconRes: Int
    get() = when (this) {
        FilterTab.FOOD -> R.drawable.ic_map_tab_eat
        FilterTab.BUILDINGS -> R.drawable.ic_map_tab_buildings
        FilterTab.HOSTELS -> R.drawable.ic_map_tab_hostels
        FilterTab.OTHERS -> R.drawable.ic_map_tab_others
        FilterTab.STRUCTURES -> R.drawable.ic_map_tab_structure
    }

private val FilterTab.nameRes: Int
    get() = when (this) {
        FilterTab.FOOD -> Strings.map_tab_name_eat
        FilterTab.BUILDINGS -> Strings.map_tab_name_buildings
        FilterTab.HOSTELS -> Strings.map_tab_name_hostels
        FilterTab.OTHERS -> Strings.map_tab_name_others
        FilterTab.STRUCTURES -> Strings.map_tab_name_structures
    }

@Composable
@PreviewLightDark
private fun FilterTabsPreview() {
    MpeixPreview {
        FilterTabs(
            onTabClick = { /* no-op */ },
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}
