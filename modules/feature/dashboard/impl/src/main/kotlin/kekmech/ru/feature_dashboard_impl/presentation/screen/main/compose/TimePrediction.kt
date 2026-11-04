package kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Time left until the start of the next classes.
 */
@Composable
internal fun TimePrediction(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_time_prediction),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MpeixTheme.palette.onSurface,
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurface,
        )
    }
}

@PreviewLightDark
@Composable
private fun TimePredictionPreview() {
    MpeixTheme {
        TimePrediction(
            text = "Пара начнется через 15 часов 43 минуты",
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(vertical = 8.dp),
        )
    }
}
