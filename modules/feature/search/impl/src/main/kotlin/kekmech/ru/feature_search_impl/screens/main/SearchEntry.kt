package kekmech.ru.feature_search_impl.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.kekmech.lib_fragment.HideKeyboardOnPause
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.feature_search_impl.di.SearchDependencies
import kekmech.ru.feature_search_impl.navigation.SearchScreenKey
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent.Ui
import kekmech.ru.feature_search_impl.screens.schedule_details.ScheduleDetailsFragment
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.ShowDialog
import org.koin.compose.koinInject

@Composable
internal fun SearchEntry(key: SearchScreenKey) {
    val dependencies = koinInject<SearchDependencies>()
    val analytics = rememberScreenAnalytics("Search")
    val bottomTabsSwitcher = koinInject<BottomTabsSwitcher>()
    HideKeyboardOnPause()
    val router = LocalRouter.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ElmContent(
        key = "SearchScreen",
        storeFactory = {
            dependencies.searchStoreFactory.create(
                query = key.query,
                filter = key.filter,
            )
        },
    ) { onAccept, state, effects ->
        SearchScreen(
            onAccept = onAccept,
            state = state,
            effects = effects,
            onBackClick = { router.executeCommand(PopBackStack()) },
            onFilterClick = { filter ->
                analytics.sendClick("SearchFilter_${filter.type}")
                onAccept(Ui.Action.SelectFilter(filter))
            },
            onNoteClick = { note ->
                analytics.sendClick("SearchNote")
                dependencies.notesFeatureLauncher.launchAllNotes(note)
            },
            onMapMarkerClick = { marker ->
                analytics.sendClick("SearchMarker")
                dependencies.mapFeatureLauncher.selectPlace(marker.uid)
                keyboardController?.hide()
                // Переключение таба заменяет весь back stack, экран поиска закроется сам
                bottomTabsSwitcher.changeTab(BottomTab.MAP)
            },
            onScheduleClick = { searchResult ->
                analytics.sendClick("ScheduleDetails")
                keyboardController?.hide()
                router.executeCommand(ShowDialog {
                    ScheduleDetailsFragment.newInstance(searchResult)
                })
            },
        )
    }
}
