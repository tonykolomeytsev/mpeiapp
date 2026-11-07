package kekmech.ru.feature_map_impl.presentation.screen.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_map_impl.DeeplinkHelper
import kekmech.ru.feature_map_impl.di.MapDependencies
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapEvent.Ui
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapStoreFactory
import kekmech.ru.feature_map_impl.presentation.screen.main.view.MarkersBitmapFactory
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject

@Composable
internal fun MapEntry() {
    val analytics = rememberScreenAnalytics("Map")
    val dependencies = koinInject<MapDependencies>()
    val markersBitmapFactory = koinInject<MarkersBitmapFactory>()
    val appSettingsRepository = koinInject<AppSettingsRepository>()
    val bottomTabsSwitcher = koinInject<BottomTabsSwitcher>()
    // Store outlives the tab, so map type is read on every opening to pick up changed settings
    val mapAppearanceType = remember { appSettingsRepository.getAppSettings().mapAppearanceType }
    val scrollToTopRequests = remember(bottomTabsSwitcher) {
        bottomTabsSwitcher.reselects
            .filter { it == BottomTab.MAP }
            .map { }
    }
    val activity = LocalActivity.current as ComponentActivity
    val storeFactory = koinInject<MapStoreFactory>()
    ElmContent(
        key = "MapScreen",
        storeFactory = { storeFactory.create() },
        viewModelStoreOwner = activity,
        savedStateRegistryOwner = activity,
    ) { onAccept, state, effects ->
        var isMapLoaded by remember { mutableStateOf(false) }
        LaunchedEffect(state.markers, state.selectedTab, isMapLoaded) {
            if (isMapLoaded) {
                DeeplinkHelper.handleDeeplinkIfNecessary(dependencies.deeplinkDelegate, state, onAccept)
            }
        }
        MapScreen(
            state = state,
            effects = effects,
            mapAppearanceType = mapAppearanceType,
            // Factory draws every marker into the same bitmap, fromBitmap() copies it right away
            markerIcon = { BitmapDescriptorFactory.fromBitmap(markersBitmapFactory.getBitmap(it)) },
            scrollToTopRequests = scrollToTopRequests,
            onAction = { action ->
                when (action) {
                    is MapAction.TabClick -> {
                        analytics.sendClick("Tab_${action.tab}")
                        onAccept(Ui.Action.SelectTab(action.tab))
                    }

                    is MapAction.MarkerClick -> {
                        analytics.sendClick("ListMarker_(${action.marker.name})")
                        onAccept(Ui.Action.OnListMarkerSelected(action.marker))
                    }

                    is MapAction.ReloadClick -> {
                        analytics.sendClick("MapReload")
                        onAccept(Ui.Action.Reload)
                    }

                    is MapAction.SheetDragged -> analytics.sendScroll("MapBottomSheet")
                    is MapAction.MapLoaded -> isMapLoaded = true
                }
            },
        )
    }
}
