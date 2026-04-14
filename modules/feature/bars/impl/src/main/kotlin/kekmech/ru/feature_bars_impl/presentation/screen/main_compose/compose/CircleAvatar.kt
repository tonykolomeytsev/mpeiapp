package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun CircleAvatar(
    name: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .background(
                color = MpeixTheme.palette.surfacePlus3,
                shape = RoundedCornerShape(100),
            ),
    ) {
        val initials = remember(name) {
            if (name.isNotBlank()) {
                name.split(" ")
                    .map { it.first() }
                    .take(2)
                    .joinToString("")
            } else ""
        }
        if (initials.isEmpty()) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = MpeixTheme.palette.content,
                modifier = Modifier.size(24.dp),
            )
        } else {
            Text(
                text = initials,
                style = MpeixTheme.typography.paragraphBig.copy(fontWeight = FontWeight.W500),
                color = MpeixTheme.palette.content,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CircleAvatarPreview() {
    MpeixTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(MpeixTheme.palette.background)
                .padding(16.dp),
        ) {
            CircleAvatar(name = "Вася Пупкин")
            CircleAvatar(name = "Вася Пупкин Пупкинович")
            CircleAvatar(name = "Иван")
            CircleAvatar(name = "")
        }
    }
}