package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_bars_impl.R
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.ui_theme.typography.RobotoFontFamily
import kekmech.ru.res_icons.R.drawable as Icons
import kekmech.ru.res_strings.R.string as Strings

private val LegacyH2 = TextStyle(
    fontFamily = RobotoFontFamily,
    lineHeight = 28.sp,
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
)

@Composable
internal fun UserHeader(
    name: String?,
    group: String?,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Row(
        modifier = modifier
            .padding(top = 6.dp, bottom = 12.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp)
                .weight(1f),
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(
                            color = MpeixTheme.palette.shimmer,
                            shape = RoundedCornerShape(8.dp),
                        )
                )
            } else {
                Text(
                    text = name ?: stringResource(Strings.bars_stub_student_name),
                    style = LegacyH2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (group != null && !isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(Icons.ic_groups_black_24),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MpeixTheme.palette.contentVariant,
                    )
                    Text(
                        text = group,
                        style = MpeixTheme.typography.labelBig,
                        color = MpeixTheme.palette.contentVariant,
                    )
                }
            }
        }
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.padding(top = 4.dp, end = 4.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings_24),
                contentDescription = null,
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun UserHeaderPreviewEmpty() {
    MpeixTheme {
        UserHeader(
            name = null,
            group = null,
            onSettingsClick = { /* no-op */ },
        )
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun UserHeaderPreviewFilled() {
    MpeixTheme {
        UserHeader(
            name = "Антон Коломейцев",
            group = "С-12-16",
            onSettingsClick = { /* no-op */ },
        )
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun UserHeaderPreviewShimmered() {
    MpeixTheme {
        UserHeader(
            name = "Антон Коломейцев",
            group = "С-12-16",
            isLoading = true,
            onSettingsClick = { /* no-op */ },
        )
    }
}