package kekmech.ru.feature_app_update_impl.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonPalette
import kekmech.ru.ui_kit_dialogs.BottomSheet
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.coreui.R.drawable as CoreUiDrawables
import kekmech.ru.res_strings.R.string as Strings

// Icon is placed half above the sheet's top edge
private val IconSize = 104.dp
private val IconOverlap = 48.dp

@Composable
internal fun ForceUpdateScreen(
    description: String,
    onUpdateClick: () -> Unit,
    onLaterClick: () -> Unit,
) {
    Box(contentAlignment = Alignment.TopCenter) {
        BottomSheet(
            modifier = Modifier
                .padding(top = IconOverlap)
                .padding(horizontal = 8.dp),
            // Drag handle would be hidden under the icon anyway
            showDragHandle = false,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(IconSize - IconOverlap))
                Text(
                    text = stringResource(Strings.force_update_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = MpeixTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                if (description.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        style = MpeixTheme.typography.bodyLarge,
                        color = MpeixTheme.palette.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Button(
                        text = stringResource(Strings.force_update_later),
                        onClick = onLaterClick,
                        modifier = Modifier.weight(1f),
                        palette = ButtonPalette.Secondary,
                    )
                    Button(
                        text = stringResource(Strings.blocking_update_update),
                        onClick = onUpdateClick,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
        Image(
            painter = painterResource(CoreUiDrawables.splash),
            contentDescription = null,
            modifier = Modifier.size(IconSize),
        )
    }
}

@Composable
@PreviewLightDark
private fun ForceUpdateScreenPreview() {
    MpeixPreview {
        ForceUpdateScreen(
            description = "Исправлены ошибки и улучшена стабильность",
            onUpdateClick = { /* no-op */ },
            onLaterClick = { /* no-op */ },
        )
    }
}
