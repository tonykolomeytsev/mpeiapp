package kekmech.ru.feature_notes_impl.presentation.screen.note_list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.presentation.compose.lazyKey
import kekmech.ru.ui_kit_dialogs.BottomSheet
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_lists.SwipeToDeleteItem
import kekmech.ru.ui_kit_notes.NoteItem
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDateTime
import kekmech.ru.res_strings.R.plurals as Plurals
import kekmech.ru.res_strings.R.string as Strings

private val SheetMinHeight = 500.dp
private const val PressedScale = 0.9f

/**
 * Bottom sheet with notes attached to the selected classes.
 */
@Composable
internal fun NoteListScreen(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit,
    onCreateNoteClick: () -> Unit,
) {
    BottomSheet(modifier = Modifier.heightIn(min = SheetMinHeight)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(rememberNestedScrollInteropConnection()),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item(key = "header") {
                SectionHeader(
                    title = stringResource(Strings.note_list_section_header),
                    subtitle = if (notes.isEmpty()) {
                        stringResource(Strings.note_list_section_subtitle_empty)
                    } else {
                        pluralStringResource(Plurals.note_list_section_subtitle, notes.size, notes.size)
                    },
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            items(notes, key = { it.lazyKey }) { note ->
                SwipeToDeleteItem(
                    onDelete = { onNoteDelete(note) },
                    modifier = Modifier
                        .animateItem()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                ) {
                    NoteItem(
                        content = note.content,
                        date = note.dateTime.toLocalDate(),
                        disciplineName = note.classesName,
                        onClick = { onNoteClick(note) },
                    )
                }
            }
            item(key = "create_note") {
                CreateNoteButton(
                    onClick = onCreateNoteClick,
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        // 24dp gap to the content above, like in the old XML layout
                        .padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun CreateNoteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) PressedScale else 1f,
        label = "CreateNoteButtonScale",
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .background(MpeixTheme.palette.primary, CircleShape)
                    .padding(4.dp),
                tint = MpeixTheme.palette.onPrimary,
            )
            Text(
                text = stringResource(Strings.note_list_section_add),
                style = MpeixTheme.typography.titleSmall,
                color = MpeixTheme.palette.primary,
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun NoteListScreenPreview() {
    val now = LocalDateTime.now()
    MpeixPreview {
        NoteListScreen(
            notes = listOf(
                Note("Сдать типовой расчет №2", now, "Технология металлов", target = 0, id = 1),
                Note("Взять с собой калькулятор", now, "Технология металлов", target = 0, id = 2),
            ),
            onNoteClick = { /* no-op */ },
            onNoteDelete = { /* no-op */ },
            onCreateNoteClick = { /* no-op */ },
        )
    }
}

@Composable
@PreviewLightDark
private fun NoteListScreenEmptyPreview() {
    MpeixPreview {
        NoteListScreen(
            notes = emptyList(),
            onNoteClick = { /* no-op */ },
            onNoteDelete = { /* no-op */ },
            onCreateNoteClick = { /* no-op */ },
        )
    }
}
