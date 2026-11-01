package kekmech.ru.feature_notes_impl.presentation.screen.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_notes.noteBackground
import kekmech.ru.ui_kit_notes.relativeDateString
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDateTime
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun NoteEditScreen(
    note: Note,
    onBackClick: () -> Unit,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    // Initial content only, the store keeps the actual one
    val content = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(note.content, selection = TextRange(note.content.length)))
    }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIconButton(onBackClick)
                },
                title = stringResource(Strings.note_edit_screen_title),
            )
        },
        bottomBar = {
            Button(
                text = stringResource(Strings.note_edit_button_save),
                onClick = onSaveClick,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .adaptiveContentWidth()
                    .padding(16.dp),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .adaptiveContentWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = relativeDateString(note.dateTime.toLocalDate()),
                style = MpeixTheme.typography.titleSmall,
                color = MpeixTheme.palette.primary,
            )
            Text(
                text = note.classesName,
                style = MpeixTheme.typography.titleLarge,
                color = MpeixTheme.palette.onSurface,
            )
            Spacer(Modifier.weight(1f))
            BasicTextField(
                value = content.value,
                onValueChange = { value ->
                    if (value.text != content.value.text) onContentChange(value.text)
                    content.value = value
                },
                // Takes all the remaining space, so the note is attached right above the save button
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .noteBackground(
                        color = MpeixTheme.palette.secondary,
                        foldColor = MpeixTheme.palette.outlineVariant,
                    )
                    .padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
                textStyle = MpeixTheme.typography.bodyLarge.copy(color = MpeixTheme.palette.onSurface),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                cursorBrush = SolidColor(MpeixTheme.palette.primary),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun NoteEditScreenPreview() {
    MpeixPreview {
        NoteEditScreen(
            note = Note(
                content = "Сдать типовой расчет №2",
                dateTime = LocalDateTime.now().plusDays(1),
                classesName = "Технология металлов",
                target = 0,
            ),
            onBackClick = { /* no-op */ },
            onContentChange = { /* no-op */ },
            onSaveClick = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun NoteEditScreenTabletPreview() {
    NoteEditScreenPreview()
}
