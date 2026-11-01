package kekmech.ru.feature_app_settings_impl.presentation.screens.select_option

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_app_settings_impl.R
import kekmech.ru.ui_kit_dialogs.BottomSheet
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Immutable
internal data class SelectableOption(
    val code: String,
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int? = null,
)

/**
 * Bottom sheet with single choice list of [options], e.g. app language or map type.
 */
@Composable
internal fun SelectOptionScreen(
    title: String,
    options: List<SelectableOption>,
    selectedCode: String,
    onOptionClick: (SelectableOption) -> Unit,
    footer: String? = null,
) {
    BottomSheet {
        SectionHeader(title = title)
        Spacer(Modifier.height(4.dp))
        options.forEach { option ->
            ListItem(
                headlineText = stringResource(option.titleRes),
                modifier = Modifier.clickable { onOptionClick(option) },
                leadingContent = option.iconRes?.let { iconRes ->
                    { Image(painterResource(iconRes)) }
                },
                trailingContent = {
                    SelectionIndicator(selected = option.code == selectedCode)
                },
            )
        }
        if (footer != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = footer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
@PreviewLightDark
private fun SelectOptionScreenPreview() {
    MpeixPreview {
        SelectOptionScreen(
            title = stringResource(Strings.change_language_screen_title),
            options = listOf(
                SelectableOption(
                    code = "ru_RU",
                    titleRes = Strings.change_language_description_russian,
                    iconRes = R.drawable.ic_russian,
                ),
                SelectableOption(
                    code = "en_US",
                    titleRes = Strings.change_language_description_english,
                    iconRes = R.drawable.ic_english,
                ),
            ),
            selectedCode = "ru_RU",
            onOptionClick = { /* no-op */ },
            footer = stringResource(Strings.change_language_disclaimer),
        )
    }
}
