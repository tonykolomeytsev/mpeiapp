package kekmech.ru.feature_notes_impl.presentation.screen.all_notes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.presentation.compose.EmptyState
import kekmech.ru.feature_notes_impl.presentation.compose.lazyKey
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_lists.SwipeToDeleteItem
import kekmech.ru.ui_kit_notes.NoteItem
import kekmech.ru.ui_kit_notes.NoteItemShimmer
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import java.time.LocalDateTime
import kekmech.ru.res_icons.R.drawable as Icons
import kekmech.ru.res_strings.R.string as Strings

private const val SHIMMER_ITEM_COUNT = 3

/**
 * @param notes all notes, or null while notes are loading
 * @param selectedNote note to scroll to after notes are loaded, see [onSelectedNoteShown]
 */
@Composable
internal fun AllNotesScreen(
    notes: List<Note>?,
    selectedNote: Note?,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit,
    onSelectedNoteShown: () -> Unit,
) {
    val listItems = remember(notes) {
        notes?.let { AllNotesListConverter().map(it) { note -> note.lazyKey } }
    }
    val listState = rememberLazyListState()
    val currentOnSelectedNoteShown by rememberUpdatedState(onSelectedNoteShown)
    LaunchedEffect(listItems, selectedNote) {
        if (listItems.isNullOrEmpty() || selectedNote == null) return@LaunchedEffect
        val index = listItems.indexOfFirst {
            it is AllNotesListItem.NoteEntry && it.note == selectedNote
        }
        if (index != -1) listState.scrollToItem(index)
        currentOnSelectedNoteShown()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIconButton(onBackClick)
                },
                title = stringResource(Strings.all_notes_screen_title),
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            painter = painterResource(Icons.ic_search_24),
                            contentDescription = stringResource(Strings.all_notes_search_menu_item),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .adaptiveContentWidth(),
            state = listState,
            contentPadding = innerPadding,
        ) {
            when {
                listItems == null -> items(SHIMMER_ITEM_COUNT) {
                    NoteItemShimmer(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    )
                }

                listItems.isEmpty() -> item(key = "empty_state") {
                    EmptyState(
                        title = stringResource(Strings.all_notes_empty_state_title),
                        subtitle = stringResource(Strings.all_notes_empty_state_subtitle),
                    )
                }

                else -> items(listItems, key = { it.key }) { listItem ->
                    when (listItem) {
                        is AllNotesListItem.Header -> SectionHeader(
                            title = stringResource(listItem.titleRes),
                            modifier = Modifier
                                .animateItem()
                                .padding(top = 8.dp, bottom = 4.dp),
                        )

                        is AllNotesListItem.NoteEntry -> SwipeToDeleteItem(
                            onDelete = { onNoteDelete(listItem.note) },
                            modifier = Modifier
                                .animateItem()
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        ) {
                            NoteItem(
                                content = listItem.note.content,
                                date = listItem.note.dateTime.toLocalDate(),
                                disciplineName = listItem.note.classesName,
                                onClick = { onNoteClick(listItem.note) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun AllNotesScreenPreview() {
    val now = LocalDateTime.now()
    MpeixPreview {
        AllNotesScreen(
            notes = listOf(
                Note("Сдать типовой расчет №2", now.plusDays(1), "Технология металлов", target = 0, id = 1),
                Note("Подготовить доклад", now.plusDays(8), "Философия", target = 0, id = 2),
                Note("Взять методичку на кафедре", now.minusDays(3), "Электротехника", target = 0, id = 3),
            ),
            selectedNote = null,
            onBackClick = { /* no-op */ },
            onSearchClick = { /* no-op */ },
            onNoteClick = { /* no-op */ },
            onNoteDelete = { /* no-op */ },
            onSelectedNoteShown = { /* no-op */ },
        )
    }
}

@Composable
@PreviewLightDark
private fun AllNotesScreenLoadingPreview() {
    MpeixPreview {
        AllNotesScreen(
            notes = null,
            selectedNote = null,
            onBackClick = { /* no-op */ },
            onSearchClick = { /* no-op */ },
            onNoteClick = { /* no-op */ },
            onNoteDelete = { /* no-op */ },
            onSelectedNoteShown = { /* no-op */ },
        )
    }
}

@Composable
@PreviewLightDark
private fun AllNotesScreenEmptyPreview() {
    MpeixPreview {
        AllNotesScreen(
            notes = emptyList(),
            selectedNote = null,
            onBackClick = { /* no-op */ },
            onSearchClick = { /* no-op */ },
            onNoteClick = { /* no-op */ },
            onNoteDelete = { /* no-op */ },
            onSelectedNoteShown = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun AllNotesScreenTabletPreview() {
    AllNotesScreenPreview()
}
