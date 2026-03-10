package kekmech.ru.feature_bars_impl.presentation.screen.login.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

internal data class AccountItemUi(
    val id: String,
    val name: String,
    val group: String,
    val status: String,
)

@Composable
internal fun AccountItem(
    account: AccountItemUi,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (!isSelected) 1.dp else 2.dp,
                color = if (!isSelected) MpeixTheme.palette.outline else MpeixTheme.palette.primary,
                shape = RoundedCornerShape(8),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = account.name,
            style = MpeixTheme.typography.paragraphBig,
            color = MpeixTheme.palette.content,
        )
        Row {
            Text(
                text = account.group,
                style = MpeixTheme.typography.paragraphNormal,
                color = MpeixTheme.palette.contentVariant,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = account.status,
                style = MpeixTheme.typography.paragraphNormal,
                color = MpeixTheme.palette.contentDisabled,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccountItemPreview() {
    MpeixTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            AccountItem(
                account = AccountItemUi(
                    id = "",
                    name = "Иванов И.И.",
                    group = "А-08м-28",
                    status = "Обучается",
                ),
                isSelected = true,
                onClick = { /* no-op */ },
            )
            AccountItem(
                account = AccountItemUi(
                    id = "",
                    name = "Иванов И.И.",
                    group = "А-08-24",
                    status = "Завершил обучение",
                ),
                isSelected = false,
                onClick = { /* no-op */ },
            )
        }
    }
}