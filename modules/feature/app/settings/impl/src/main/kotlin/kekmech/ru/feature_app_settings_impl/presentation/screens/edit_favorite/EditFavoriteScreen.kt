package kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite.utils.adaptiveContentWidth
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_textfield.textfield.TextField
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.first
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun EditFavoriteScreen(
    name: String,
    initialDescription: String,
    onBackClick: () -> Unit,
    onSaveClick: (description: String) -> Unit,
) {
    val description = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = initialDescription,
                selection = TextRange(initialDescription.length),
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val windowInfo = LocalWindowInfo.current

    LaunchedEffect(Unit) {
        // Text field is composed in the same frame as fragment transaction is committed,
        // so focus request is ignored until window is focused and the screen is laid out
        snapshotFlow { windowInfo.isWindowFocused }.first { it }
        withFrameNanos { }
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIconButton(onBackClick)
                },
            )
        },
        bottomBar = {
            Button(
                text = stringResource(Strings.edit_favorite_button_continue),
                onClick = { onSaveClick(description.value.text) },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .adaptiveContentWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .adaptiveContentWidth()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = name,
                style = MpeixTheme.typography.headlineBig,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Strings.edit_favorite_screen_subtitle),
                style = MpeixTheme.typography.titleSmall,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))
            TextField(
                value = description.value,
                onValueChange = { description.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = stringResource(Strings.edit_favorite_hint),
                singleLine = true,
                // Trailing icon reserves space even when empty, so it is passed only with text
                trailingIcon = if (description.value.text.isNotEmpty()) {
                    { ClearIconButton() }
                } else {
                    null
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun EditFavoriteScreenPreview() {
    MpeixPreview {
        EditFavoriteScreen(
            name = "С-12м-20",
            initialDescription = "Моя группа",
            onBackClick = { /* no-op */ },
            onSaveClick = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun EditFavoriteScreenTabletPreview() {
    EditFavoriteScreenPreview()
}
