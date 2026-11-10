package kekmech.ru.mpeiapp.ui.main

import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation3.runtime.NavEntryDecorator
import kekmech.ru.feature_app_settings_api.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.bottomTab
import kekmech.ru.mpeiapp.R
import kekmech.ru.mpeiapp.ui.main.icons.AccountCircle
import kekmech.ru.mpeiapp.ui.main.icons.AccountCircleFilled
import kekmech.ru.mpeiapp.ui.main.icons.CalendarToday
import kekmech.ru.mpeiapp.ui.main.icons.CalendarTodayFilled
import kekmech.ru.mpeiapp.ui.main.icons.LocationOn
import kekmech.ru.mpeiapp.ui.main.icons.LocationOnFilled
import kekmech.ru.mpeiapp.ui.main.icons.ModeHeat
import kekmech.ru.mpeiapp.ui.main.icons.ModeHeatFilled
import kekmech.ru.mpeiapp.ui.main.icons.NavigationIcons
import kekmech.ru.ui_kit_navigationbar.floating.FloatingNavBar
import org.koin.compose.koinInject
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
        // Навигация плавает поверх контента, поэтому экраны табов сами добавляют отступ снизу:
        // высоту системной навигации + 80dp
        content()
        MainNavigationBar(
            selectedTab = selectedTab,
            onTabClick = onTabClick,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
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
    modifier: Modifier = Modifier,
) {
    FloatingNavBar(
        modifier = modifier
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
    ) {
        BottomTab.entries.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onTabClick(tab) },
                iconSelected = tab.iconSelected,
                iconUnselected = tab.iconUnselected,
                label = stringResource(tab.label),
            )
        }
    }
}

private val BottomTab.iconSelected: ImageVector
    get() = when (this) {
        BottomTab.DASHBOARD -> NavigationIcons.ModeHeatFilled
        BottomTab.SCHEDULE -> NavigationIcons.CalendarTodayFilled
        BottomTab.MAP -> NavigationIcons.LocationOnFilled
        BottomTab.PROFILE -> NavigationIcons.AccountCircleFilled
    }

private val BottomTab.iconUnselected: ImageVector
    get() = when (this) {
        BottomTab.DASHBOARD -> NavigationIcons.ModeHeat
        BottomTab.SCHEDULE -> NavigationIcons.CalendarToday
        BottomTab.MAP -> NavigationIcons.LocationOn
        BottomTab.PROFILE -> NavigationIcons.AccountCircle
    }

private val BottomTab.label: Int
    get() = when (this) {
        BottomTab.DASHBOARD -> Strings.navigation_name_dashboard
        BottomTab.SCHEDULE -> Strings.navigation_name_schedule
        BottomTab.MAP -> Strings.navigation_name_map
        BottomTab.PROFILE -> Strings.navigation_name_profile
    }
