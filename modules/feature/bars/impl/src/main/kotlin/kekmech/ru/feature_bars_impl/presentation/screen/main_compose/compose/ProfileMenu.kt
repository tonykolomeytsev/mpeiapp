package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun ProfileMenu(
    onClick: () -> Unit,
    name: String,
    group: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .clickable(onClick = onClick)
            .padding(end = 16.dp),
    ) {
        CircleAvatar(name = name)
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = name,
                style = MpeixTheme.typography.paragraphBig.copy(fontWeight = FontWeight.W500),
                color = MpeixTheme.palette.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = group,
                style = MpeixTheme.typography.labelBig,
                color = MpeixTheme.palette.contentDisabled,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun ProfileMenuPreview() {
    MpeixTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(MpeixTheme.palette.background)
                .padding(16.dp)
                .widthIn(max = 300.dp),
        ) {
            ProfileMenu(
                onClick = { /* no-op */ },
                name = "Антон Коломейцев",
                group = "C-12-16",
            )
            ProfileMenu(
                onClick = { /* no-op */ },
                name = "Какое-то ОченьдлинноепиздецкакоеИмяиФамилия",
                group = "C-12-16",
            )
        }
    }
}