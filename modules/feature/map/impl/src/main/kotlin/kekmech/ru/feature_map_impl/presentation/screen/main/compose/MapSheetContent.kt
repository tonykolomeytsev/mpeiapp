package kekmech.ru.feature_map_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_map_api.domain.model.Location
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_api.domain.model.MarkerType
import kekmech.ru.feature_map_impl.presentation.screen.main.SectionTitle
import kekmech.ru.feature_map_impl.presentation.screen.main.createMapSections
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.FilterTab
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_states.ErrorState
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.net.UnknownHostException

/**
 * Content of the map bottom sheet: drag handle, filter tabs and the list of places.
 */
@Composable
internal fun MapSheetContent(
    state: MapState,
    onTabClick: (FilterTab) -> Unit,
    onMarkerClick: (MapMarker) -> Unit,
    onReloadClick: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val sections = remember(state.markers, state.selectedTab) {
        createMapSections(state.markers, state.selectedTab)
    }
    Column(modifier = modifier) {
        DragHandle()
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                bottom = 96.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            ),
        ) {
            item(key = "tabs", contentType = "tabs") {
                FilterTabs(
                    onTabClick = onTabClick,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            val loadingError = state.loadingError
            if (state.markers.isEmpty() && loadingError != null) {
                item(key = "error", contentType = "error") {
                    ErrorState(
                        throwable = loadingError,
                        onReloadClick = onReloadClick,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }
            } else {
                sections.forEach { section ->
                    item(key = "header_${section.title}", contentType = "header") {
                        SectionHeader(
                            title = when (val title = section.title) {
                                is SectionTitle.Text -> title.value
                                is SectionTitle.Res -> stringResource(title.id)
                            },
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                        )
                    }
                    items(section.markers, key = { it.uid }, contentType = { "marker" }) { marker ->
                        ListItem(
                            headlineText = marker.name,
                            supportingText = marker.address,
                            modifier = Modifier.clickable { onMarkerClick(marker) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(22.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 34.dp, height = 6.dp)
                .background(MpeixTheme.palette.outlineVariant, CircleShape),
        )
    }
}

internal val PreviewAppSettings = AppSettings(
    isDarkThemeEnabled = false,
    appTheme = AppTheme.System,
    isSnowEnabled = false,
    languageCode = "ru_RU",
    showNavigationButton = false,
    autoHideBottomSheet = true,
    mapAppearanceType = "scheme",
)

internal val PreviewMarkers = listOf(
    "Столовая Б-114" to "Главный корпус, корпус Б",
    "Буфет Ж-200" to "Корпус Ж",
    "Кафе \"Энергия\"" to "Красноказарменная ул., 14",
).mapIndexed { index, (name, address) ->
    MapMarker(
        uid = index.toString(),
        address = address,
        location = Location(lat = 0.0, lng = 0.0),
        name = name,
        type = MarkerType.FOOD,
        icon = null,
        tag = null,
    )
}

@Composable
@PreviewLightDark
private fun MapSheetContentPreview() {
    MpeixPreview {
        MapSheetContent(
            state = MapState(appSettings = PreviewAppSettings, markers = PreviewMarkers),
            onTabClick = { /* no-op */ },
            onMarkerClick = { /* no-op */ },
            onReloadClick = { /* no-op */ },
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}

@Composable
@PreviewLightDark
private fun MapSheetContentErrorPreview() {
    MpeixPreview {
        MapSheetContent(
            state = MapState(appSettings = PreviewAppSettings, loadingError = UnknownHostException()),
            onTabClick = { /* no-op */ },
            onMarkerClick = { /* no-op */ },
            onReloadClick = { /* no-op */ },
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}
