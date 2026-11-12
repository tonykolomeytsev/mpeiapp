package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_schedule_ui.R
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

/**
 * # SelfStudyItem
 *
 * Placeholder of the day without classes: illustration, title and description,
 * centered vertically in the available space.
 *
 * Replaces the `SelfStudyAdapterItem` of the RecyclerView implementation.
 *
 * @param modifier [Modifier] to be applied to the item, it defines the size of the item
 */
@Composable
public fun SelfStudyItem(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_self_study_day),
            contentDescription = null,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Strings.schedule_item_self_study_header),
            style = MpeixTheme.typography.headlineSmall,
            color = MpeixTheme.palette.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(Strings.schedule_item_self_study_description),
            style = MpeixTheme.typography.bodyLarge,
            fontSize = 16.sp,
            color = MpeixTheme.palette.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun SelfStudyItemPreview() {
    MpeixTheme {
        SelfStudyItem(
            Modifier
                .fillMaxSize()
                .background(MpeixTheme.palette.surface)
        )
    }
}
