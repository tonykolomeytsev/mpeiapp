package kekmech.ru.feature_search_impl.screens.schedule_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kekmech.ru.feature_search_impl.navigation.ScheduleDetailsScreenKey
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEffect
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Ui
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsStoreFactory
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import org.koin.compose.koinInject

@Composable
internal fun ScheduleDetailsEntry(key: ScheduleDetailsScreenKey) {
    val storeFactory = koinInject<ScheduleDetailsStoreFactory>()
    val bottomTabsSwitcher = koinInject<BottomTabsSwitcher>()
    val analytics = rememberScreenAnalytics("SearchScheduleDetails")
    ElmContent(
        key = "ScheduleDetailsScreen",
        storeFactory = { storeFactory.create(searchResult = key.searchResult) },
    ) { onAccept, state, effects ->
        EffectHandler(effects) { effect ->
            when (effect) {
                // Switching the tab replaces the whole back stack, so the sheet closes itself
                is ScheduleDetailsEffect.CloseAndGoToSchedule -> bottomTabsSwitcher.changeTab(BottomTab.DASHBOARD)
            }
        }
        ScheduleDetailsScreen(
            ui = remember(state) { ScheduleDetailsUiConverter.map(state) },
            onAction = { action ->
                when (action) {
                    is ScheduleDetailsAction.FavoritesClick -> {
                        analytics.sendClick("AddToFavorites")
                        onAccept(Ui.Click.Favorites)
                    }

                    is ScheduleDetailsAction.DayClick -> onAccept(Ui.Click.Day(action.date))
                    is ScheduleDetailsAction.SwitchScheduleClick -> {
                        analytics.sendClick("SwitchSchedule")
                        onAccept(Ui.Click.SwitchSchedule)
                    }
                }
            },
        )
    }
}
