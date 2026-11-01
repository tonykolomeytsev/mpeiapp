package kekmech.ru.feature_search_impl.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_map_api.domain.model.Location
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_api.domain.model.MarkerType
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_search_impl.screens.main.compose.EmptyState
import kekmech.ru.feature_search_impl.screens.main.compose.FilterChips
import kekmech.ru.feature_search_impl.screens.main.compose.SearchField
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent.Ui
import kekmech.ru.feature_search_impl.item.FilterItem
import kekmech.ru.feature_search_impl.item.FilterItemType
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_notes.NoteItem
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.milliseconds
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEffect as Effect
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent as Event
import kekmech.ru.feature_search_impl.screens.main.elm.SearchState as State
import kekmech.ru.res_strings.R.string as Strings

private val InputDebounce = 300.milliseconds

@OptIn(FlowPreview::class)
@Composable
internal fun SearchScreen(
    onAccept: (Event) -> Unit,
    state: State,
    effects: Flow<Effect>,
    onBackClick: () -> Unit,
    onFilterClick: (FilterItem) -> Unit,
    onNoteClick: (Note) -> Unit,
    onMapMarkerClick: (MapMarker) -> Unit,
    onScheduleClick: (SearchResult) -> Unit,
) {
    val query = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(state.query, selection = TextRange(state.query.length)))
    }
    val listState = remember(state) { SearchListConverter().map(state) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val windowInfo = LocalWindowInfo.current
    val currentOnAccept by rememberUpdatedState(onAccept)

    LaunchedEffect(Unit) {
        // Text field is composed in the same frame as fragment transaction is committed,
        // so focus request is ignored until window is focused and the screen is laid out
        snapshotFlow { windowInfo.isWindowFocused }.first { it }
        withFrameNanos { }
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    LaunchedEffect(Unit) {
        snapshotFlow { query.value.text }
            // Initial query is searched by the store itself
            .drop(1)
            .debounce(InputDebounce)
            .distinctUntilChanged()
            .collect { text -> currentOnAccept(Ui.Action.SearchContent(text)) }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null,
                    )
                }
                SearchField(
                    value = query.value,
                    onValueChange = { query.value = it },
                    placeholder = stringResource(Strings.dashboard_search_field_text),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .adaptiveContentWidth(),
            contentPadding = innerPadding,
        ) {
            item(key = "filters") {
                FilterChips(
                    filters = state.filterItems,
                    onFilterClick = onFilterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
            }
            when (listState) {
                is SearchListState.EmptyQuery -> item(key = "empty_query") {
                    EmptyState(
                        title = stringResource(Strings.search_empty_state_title),
                        subtitle = stringResource(Strings.search_empty_state_subtitle),
                    )
                }

                is SearchListState.NotFound -> item(key = "not_found") {
                    EmptyState(
                        title = stringResource(Strings.search_not_found_state_title),
                        subtitle = stringResource(Strings.search_not_found_state_subtitle),
                    )
                }

                is SearchListState.Results -> items(listState.items, key = { it.key }) { item ->
                    SearchListItemContent(
                        item = item,
                        onNoteClick = onNoteClick,
                        onMapMarkerClick = onMapMarkerClick,
                        onScheduleClick = onScheduleClick,
                    )
                }
            }
        }
    }

    EffectHandler(effects) { effect ->
        when (effect) {
            is Effect.SetInitialQuery -> query.value = TextFieldValue(
                text = effect.query,
                selection = TextRange(effect.query.length),
            )
        }
    }
}

@Composable
private fun SearchListItemContent(
    item: SearchListItem,
    onNoteClick: (Note) -> Unit,
    onMapMarkerClick: (MapMarker) -> Unit,
    onScheduleClick: (SearchResult) -> Unit,
) {
    when (item) {
        is SearchListItem.Header -> SectionHeader(
            title = stringResource(item.titleRes),
            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
        )

        is SearchListItem.NoteEntry -> NoteItem(
            content = item.note.content,
            date = item.note.dateTime.toLocalDate(),
            disciplineName = item.note.classesName,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            onClick = { onNoteClick(item.note) },
        )

        is SearchListItem.MapMarkerEntry -> ListItem(
            headlineText = item.marker.name,
            supportingText = item.marker.address,
            modifier = Modifier.clickable { onMapMarkerClick(item.marker) },
        )

        is SearchListItem.ScheduleEntry -> ListItem(
            headlineText = item.result.name,
            supportingText = item.result.description,
            modifier = Modifier.clickable { onScheduleClick(item.result) },
        )
    }
}

@Composable
@PreviewLightDark
private fun SearchScreenPreview() {
    MpeixPreview {
        SearchScreen(
            onAccept = { /* no-op */ },
            state = State(
                query = "Тех",
                selectedFilter = FilterItemType.ALL,
                searchResultsNotes = listOf(
                    Note("Сдать типовой расчет №2", LocalDateTime.now(), "Технология металлов", target = 0, id = 1),
                ),
                searchResultsMap = listOf(
                    MapMarker(
                        uid = "1",
                        address = "Красноказарменная ул., 13с4",
                        location = Location(0.0, 0.0),
                        name = "Корпус Т",
                        type = MarkerType.OTHER,
                        icon = null,
                        tag = null,
                    ),
                ),
                searchResultsPersons = listOf(
                    SearchResult("1", "Техников Иван Иванович", "Кафедра ТМ", ScheduleType.PERSON),
                ),
            ),
            effects = emptyFlow(),
            onBackClick = { /* no-op */ },
            onFilterClick = { /* no-op */ },
            onNoteClick = { /* no-op */ },
            onMapMarkerClick = { /* no-op */ },
            onScheduleClick = { /* no-op */ },
        )
    }
}

@Composable
@PreviewLightDark
private fun SearchScreenEmptyPreview() {
    MpeixPreview {
        SearchScreen(
            onAccept = { /* no-op */ },
            state = State(query = ""),
            effects = emptyFlow(),
            onBackClick = { /* no-op */ },
            onFilterClick = { /* no-op */ },
            onNoteClick = { /* no-op */ },
            onMapMarkerClick = { /* no-op */ },
            onScheduleClick = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun SearchScreenTabletPreview() {
    SearchScreenPreview()
}
