package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

private val TimeColumnWidth = 76.dp
private val ItemShape = RoundedCornerShape(12.dp)

/**
 * # LunchItem
 *
 * Lunch break between the second and the third classes of the day.
 * The whole item is clickable, it should open the canteens on the map.
 *
 * Replaces the `LunchAdapterItem` of the RecyclerView implementation.
 *
 * @param onClick called when the item is clicked
 * @param modifier [Modifier] to be applied to the item
 */
@Composable
public fun LunchItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ItemShape)
            .clickable(onClick = onClick)
            .padding(top = 16.dp, bottom = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .width(TimeColumnWidth)
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Strings.schedule_item_lunch_time_start),
                style = MpeixTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = MpeixTheme.palette.onSurface,
            )
            Text(
                text = stringResource(Strings.schedule_item_lunch_time_end),
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 32.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Strings.schedule_item_lunch_header),
                style = MpeixTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = MpeixTheme.palette.onSurface,
            )
            Text(
                text = stringResource(Strings.schedule_item_lunch_description),
                style = MpeixTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MpeixTheme.palette.primary,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LunchItemPreview() {
    MpeixTheme {
        LunchItem(
            onClick = {},
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
        )
    }
}
