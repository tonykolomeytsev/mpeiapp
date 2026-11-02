package kekmech.ru.mpeiapp.ui.main

import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation3.runtime.NavEntryDecorator
import kekmech.ru.feature_app_settings_api.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.bottomTab
import kekmech.ru.mpeiapp.R
import kekmech.ru.ui_kit_navigationbar.NavigationBar
import kekmech.ru.ui_kit_navigationbar.NavigationBarItem
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject
import kekmech.ru.res_icons.R.drawable as Icons
import kekmech.ru.res_strings.R.string as Strings

/**
 * Рисует нижнюю навигацию вокруг корневых экранов табов.
 *
 * Навигация рисуется внутри экрана, поэтому экраны, открытые поверх таба,
 * перекрывают ее так же, как раньше перекрывали MainFragment.
 */
@Composable
fun rememberMainTabsNavEntryDecorator(): NavEntryDecorator<ScreenKey> {
    val bottomTabsSwitcher = koinInject<BottomTabsSwitcher>()
    val snowFlakesFeatureToggle = koinInject<IsSnowFlakesEnabledFeatureToggle>()
    val appSettingsRepository = koinInject<AppSettingsRepository>()
    val isSnowFlakesEnabled = remember {
        snowFlakesFeatureToggle.value && appSettingsRepository.getAppSettings().isSnowEnabled
    }
    return remember(bottomTabsSwitcher, isSnowFlakesEnabled) {
        NavEntryDecorator { entry ->
            val selectedTab = entry.bottomTab
            if (selectedTab == null) {
                entry.Content()
            } else {
                MainTabsScaffold(
                    selectedTab = selectedTab,
                    onTabClick = bottomTabsSwitcher::changeTab,
                    isSnowFlakesEnabled = isSnowFlakesEnabled,
                    content = { entry.Content() },
                )
            }
        }
    }
}

@Composable
private fun MainTabsScaffold(
    selectedTab: BottomTab,
    onTabClick: (BottomTab) -> Unit,
    isSnowFlakesEnabled: Boolean,
    content: @Composable () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) {
                content()
            }
            MpeixTheme {
                MainNavigationBar(
                    selectedTab = selectedTab,
                    onTabClick = onTabClick,
                )
            }
        }
        if (isSnowFlakesEnabled) {
            AndroidView(
                factory = { context -> View.inflate(context, R.layout.item_snow_flakes, null) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun MainNavigationBar(
    selectedTab: BottomTab,
    onTabClick: (BottomTab) -> Unit,
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        BottomTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = tab == selectedTab,
                onClick = { onTabClick(tab) },
                icon = {
                    val painter = painterResource(tab.icon)
                    Icon(painterSelected = painter, painterUnselected = painter)
                },
                label = stringResource(tab.label),
            )
        }
    }
}

private val BottomTab.icon: Int
    get() = when (this) {
        BottomTab.DASHBOARD -> Icons.ic_navigation_dashboard
        BottomTab.SCHEDULE -> Icons.ic_navigation_schedule
        BottomTab.MAP -> Icons.ic_navigation_map
        BottomTab.PROFILE -> Icons.ic_navigation_profile
    }

private val BottomTab.label: Int
    get() = when (this) {
        BottomTab.DASHBOARD -> Strings.navigation_name_dashboard
        BottomTab.SCHEDULE -> Strings.navigation_name_schedule
        BottomTab.MAP -> Strings.navigation_name_map
        BottomTab.PROFILE -> Strings.navigation_name_profile
    }
