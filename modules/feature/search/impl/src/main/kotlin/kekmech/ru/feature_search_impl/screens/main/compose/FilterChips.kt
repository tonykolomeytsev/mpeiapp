package kekmech.ru.feature_search_impl.screens.main.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_search_impl.item.FilterItem
import kekmech.ru.ui_theme.theme.MpeixTheme

private val ChipShape = RoundedCornerShape(16.dp)

@Composable
internal fun FilterChips(
    filters: List<FilterItem>,
    onFilterClick: (FilterItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(filters, key = { it.type }) { filter ->
            FilterChip(
                selected = filter.isSelected,
                onClick = { onFilterClick(filter) },
                label = {
                    Text(
                        text = stringResource(filter.type.nameRes),
                        style = MpeixTheme.typography.bodyMedium,
                        // FilterChip has 8dp label padding, original XML chip had 12dp
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                },
                shape = ChipShape,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MpeixTheme.palette.surfaceContainerHighest,
                    labelColor = MpeixTheme.palette.onSurface,
                    selectedContainerColor = MpeixTheme.palette.primary,
                    selectedLabelColor = MpeixTheme.palette.onPrimary,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = filter.isSelected,
                    borderColor = MpeixTheme.palette.outlineVariant,
                    selectedBorderWidth = 0.dp,
                ),
            )
        }
    }
}
