package kekmech.ru.feature_map_impl.presentation.screen.main

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_impl.presentation.screen.main.compose.DefaultCameraPosition
import kekmech.ru.feature_map_impl.presentation.screen.main.compose.MapContent
import kekmech.ru.feature_map_impl.presentation.screen.main.compose.MapSheetContent
import kekmech.ru.feature_map_impl.presentation.screen.main.compose.PreviewAppSettings
import kekmech.ru.feature_map_impl.presentation.screen.main.compose.PreviewMarkers
import kekmech.ru.feature_map_impl.presentation.screen.main.compose.animateTo
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.FilterTab
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.toMarkerType
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.layout.ContentMaxWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEffect as Effect
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState as State
import kekmech.ru.res_strings.R.string as Strings

private const val MAP_START_UP_DELAY = 100L // ms
private const val MAX_OVERLAY_ALPHA = 0.5f
private val SheetPeekHeight = 200.dp
private val SheetCornerRadius = 16.dp

internal sealed interface MapAction {
    data class TabClick(val tab: FilterTab) : MapAction
    data class MarkerClick(val marker: MapMarker) : MapAction
    data object ReloadClick : MapAction
    data object SheetDragged : MapAction
    data object MapLoaded : MapAction
}

/**
 * Switching tabs replaces the whole back stack, so `rememberSaveable` state of the tab is lost.
 * Camera position lives as long as the process, like states of other tabs in `TabStateStore`.
 */
private object SavedCameraPosition {
    var value: CameraPosition? = null
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
internal fun MapScreen(
    state: State,
    effects: Flow<Effect>,
    mapAppearanceType: String,
    markerIcon: (MapMarker) -> BitmapDescriptor,
    scrollToTopRequests: Flow<Unit>,
    onAction: (MapAction) -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val isInPreview = LocalInspectionMode.current
    val scope = rememberCoroutineScope()
    val currentOnAction by rememberUpdatedState(onAction)

    val scaffoldState = rememberBottomSheetScaffoldState()
    val sheetState = scaffoldState.bottomSheetState
    val listState = rememberLazyListState()
    val cameraPositionState = rememberCameraPositionState {
        position = SavedCameraPosition.value ?: DefaultCameraPosition
    }
    val visibleMarkers = remember(state.markers, state.selectedTab) {
        state.markers.filter { it.type == state.selectedTab.toMarkerType() }
    }
    val markerStates = remember(state.markers) {
        state.markers.associate { it.uid to MarkerState(LatLng(it.location.lat, it.location.lng)) }
    }
    // Map creation is heavy, so on the first opening the tab appears first and the map right after it
    var isMapVisible by remember { mutableStateOf(SavedCameraPosition.value != null) }
    // Sheet moves by itself after list clicks and tab reselects, only user drags go to analytics
    var isSheetMovedByApp by remember { mutableStateOf(false) }
    val collapseSheet: suspend () -> Unit = {
        isSheetMovedByApp = true
        try {
            sheetState.partialExpand()
            listState.scrollToItem(0)
        } finally {
            isSheetMovedByApp = false
        }
    }

    LaunchedEffect(Unit) {
        if (!isMapVisible) {
            delay(MAP_START_UP_DELAY)
            isMapVisible = true
        }
    }
    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.targetValue }
            .drop(1)
            .collect { if (!isSheetMovedByApp) currentOnAction(MapAction.SheetDragged) }
    }
    LaunchedEffect(scrollToTopRequests) {
        scrollToTopRequests.collect { collapseSheet() }
    }
    DisposableEffect(cameraPositionState) {
        onDispose { SavedCameraPosition.value = cameraPositionState.position }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        // Expanded sheet stops under the status bar, while the map is drawn behind it
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        // Collapsed sheet peeks above the floating navigation bar
        val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val sheetPeekHeight = SheetPeekHeight + navigationBarHeight
        val sheetHeight = maxHeight - statusBarHeight
        val collapsedOffset = with(density) { (maxHeight - sheetPeekHeight).toPx() }
        val expandedOffset = with(density) { statusBarHeight.toPx() }
        val cornerRadius by animateDpAsState(
            targetValue = if (sheetState.currentValue == SheetValue.Expanded) 0.dp else SheetCornerRadius,
            label = "SheetCornerRadius",
        )
        BottomSheetScaffold(
            sheetContent = {
                MapSheetContent(
                    state = state,
                    onTabClick = { onAction(MapAction.TabClick(it)) },
                    onMarkerClick = { onAction(MapAction.MarkerClick(it)) },
                    onReloadClick = { onAction(MapAction.ReloadClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sheetHeight),
                    listState = listState,
                )
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = sheetPeekHeight,
            sheetMaxWidth = ContentMaxWidth,
            sheetShape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
            sheetContainerColor = MpeixTheme.palette.surface,
            sheetContentColor = MpeixTheme.palette.onSurface,
            sheetDragHandle = null,
            containerColor = Color.Transparent,
        ) {
            Box(Modifier.fillMaxSize()) {
                if (isInPreview) {
                    // GoogleMap can't be rendered in previews
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MpeixTheme.palette.surfaceContainer)
                    )
                } else if (isMapVisible) {
                    MapContent(
                        markers = visibleMarkers,
                        markerStates = markerStates,
                        cameraPositionState = cameraPositionState,
                        mapAppearanceType = mapAppearanceType,
                        markerIcon = markerIcon,
                        bottomPadding = sheetPeekHeight,
                        onMapLoaded = { onAction(MapAction.MapLoaded) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                // Fades the map out while the sheet is pulled up, has no pointer input,
                // so touches still reach the map
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = sheetState.expandFraction(collapsedOffset, expandedOffset) *
                                MAX_OVERLAY_ALPHA
                        }
                        .background(MpeixTheme.palette.surface)
                )
            }
        }
    }

    EffectHandler(effects) { effect ->
        when (effect) {
            is Effect.AnimateCameraToPlace -> {
                markerStates[effect.mapMarker.uid]?.let { markerState ->
                    markerState.showInfoWindow()
                    scope.launch { cameraPositionState.animateTo(markerState.position) }
                }
                if (effect.collapseBottomSheet) {
                    scope.launch { collapseSheet() }
                }
            }

            is Effect.ShowLoadingError -> Toast
                .makeText(context, Strings.map_loading_error_message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

/**
 * 0 when the sheet is collapsed, 1 when it is fully expanded
 */
@OptIn(ExperimentalMaterial3Api::class)
private fun SheetState.expandFraction(collapsedOffset: Float, expandedOffset: Float): Float {
    // Offset is unknown until the first layout of the sheet
    val offset = runCatching { requireOffset() }.getOrNull() ?: return 0f
    if (collapsedOffset <= expandedOffset) return 0f
    return ((collapsedOffset - offset) / (collapsedOffset - expandedOffset)).coerceIn(0f, 1f)
}

@Composable
@PreviewLightDark
private fun MapScreenPreview() {
    MpeixPreview {
        MapScreen(
            state = State(appSettings = PreviewAppSettings, markers = PreviewMarkers),
            effects = emptyFlow(),
            mapAppearanceType = "scheme",
            markerIcon = { error("Map is not rendered in previews") },
            scrollToTopRequests = emptyFlow(),
            onAction = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun MapScreenTabletPreview() {
    MapScreenPreview()
}
