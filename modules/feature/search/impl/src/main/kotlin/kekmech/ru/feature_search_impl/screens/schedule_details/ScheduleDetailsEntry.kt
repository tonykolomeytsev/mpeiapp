package kekmech.ru.feature_search_impl.screens.schedule_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kekmech.ru.feature_map_api.MapFeatureLauncher
import kekmech.ru.feature_schedule_ui.compose.ScheduleDaysAction
import kekmech.ru.feature_search_impl.navigation.ScheduleDetailsScreenKey
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Ui
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsStoreFactory
import kekmech.ru.lib_analytics_android.Analytics
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.LocalRouter
import kekmech.ru.lib_navigation.PopBackStack
import org.koin.compose.koinInject

private const val MAP_FOOD_TAB = "FOOD"

@Composable
internal fun ScheduleDetailsEntry(key: ScheduleDetailsScreenKey) {
    val storeFactory = koinInject<ScheduleDetailsStoreFactory>()
    val mapFeatureLauncher = koinInject<MapFeatureLauncher>()
    val bottomTabsSwitcher = koinInject<BottomTabsSwitcher>()
    val router = LocalRouter.current
    val analytics = rememberScreenAnalytics("SearchScheduleDetails")
    ElmContent(
        key = "ScheduleDetailsScreen",
        storeFactory = { storeFactory.create(searchResult = key.searchResult) },
    ) { onAccept, state, _ ->
        ScheduleDetailsScreen(
            ui = remember(state) { ScheduleDetailsUiConverter.map(state) },
            onAction = { action ->
                when (action) {
                    is ScheduleDetailsAction.BackClick -> router.executeCommand(PopBackStack())
                    is ScheduleDetailsAction.FavoritesClick -> {
                        analytics.sendClick("AddToFavorites")
                        onAccept(Ui.Click.Favorites)
                    }

                    is ScheduleDetailsAction.WeekSelected -> {
                        analytics.sendClick("Week_${action.weekOffset}")
                        onAccept(Ui.Action.SelectWeek(action.weekOffset))
                    }

                    is ScheduleDetailsAction.WeeksDragged -> analytics.sendScroll("WeeksRecyclerView")
                    is ScheduleDetailsAction.DayClick -> {
                        analytics.sendClick("Day_${action.date}")
                        onAccept(Ui.Click.Day(action.date))
                    }

                    is ScheduleDetailsAction.Days -> handleDaysAction(action.action, onAccept, analytics) {
                        // Like the lunch of the Schedule tab, opens the canteens on the map.
                        // Switching the tab replaces the whole back stack, so the screen closes itself
                        mapFeatureLauncher.selectTab(MAP_FOOD_TAB)
                        bottomTabsSwitcher.changeTab(BottomTab.MAP)
                    }
                }
            },
        )
    }
}

private fun handleDaysAction(
    action: ScheduleDaysAction,
    onAccept: (Ui) -> Unit,
    analytics: Analytics,
    onLunchClick: () -> Unit,
) {
    when (action) {
        is ScheduleDaysAction.PageSelected -> onAccept(Ui.Action.PageChanged(action.weekOffset, action.dayOfWeek))
        is ScheduleDaysAction.PagerDragged -> analytics.sendScroll("WorkingDaysViewPager")
        is ScheduleDaysAction.ClassesScrolled -> onAccept(Ui.Action.ClassesScrolled(action.dy))
        is ScheduleDaysAction.LunchClick -> onLunchClick()
        is ScheduleDaysAction.ReloadClick -> {
            analytics.sendClick("ClassesReload")
            onAccept(Ui.Click.Reload)
        }
        // The classes are not clickable and the saved schedule banner is not shown on this screen
        is ScheduleDaysAction.ClassesClick, is ScheduleDaysAction.SavedScheduleBannerClick -> Unit
    }
}
