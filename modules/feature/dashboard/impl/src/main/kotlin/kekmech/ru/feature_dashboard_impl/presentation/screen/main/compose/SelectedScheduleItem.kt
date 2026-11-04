package kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_icons.R.drawable as Icons
import kekmech.ru.res_strings.R.string as Strings

/**
 * Name of the selected schedule, opens the schedule search on click.
 */
@Composable
internal fun SelectedScheduleItem(
    name: String,
    type: ScheduleType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(
                    when (type) {
                        ScheduleType.GROUP -> Icons.ic_groups_black_24
                        ScheduleType.PERSON -> R.drawable.ic_person_24
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MpeixTheme.palette.primary,
            )
            Text(
                text = name,
                modifier = Modifier.padding(start = 8.dp),
                style = MpeixTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MpeixTheme.palette.primary,
            )
        }
        Text(
            text = stringResource(Strings.dashboard_item_click_to_change_group),
            modifier = Modifier.padding(start = 24.dp),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

@PreviewLightDark
@Composable
private fun SelectedScheduleItemPreview() {
    MpeixTheme {
        Column(Modifier.background(MpeixTheme.palette.surface)) {
            SelectedScheduleItem(name = "С-12-16", type = ScheduleType.GROUP, onClick = { /* no-op */ })
            SelectedScheduleItem(
                name = "Бободжанов Абдухафиз Абдурасулович",
                type = ScheduleType.PERSON,
                onClick = { /* no-op */ },
            )
        }
    }
}
