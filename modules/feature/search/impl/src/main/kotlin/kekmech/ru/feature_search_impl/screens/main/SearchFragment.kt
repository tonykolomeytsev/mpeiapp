package kekmech.ru.feature_search_impl.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.kekmech.lib_fragment.ComposeFragment
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.ext_android.hideKeyboard
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.feature_search_impl.di.SearchDependencies
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent.Ui
import kekmech.ru.feature_search_impl.screens.schedule_details.ScheduleDetailsFragment
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.ShowDialog
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject

private const val ARG_QUERY = "Arg.Query"
private const val ARG_FILTER = "Arg.Filter"

internal class SearchFragment : ComposeFragment() {

    private val dependencies by inject<SearchDependencies>()
    private val analytics by screenAnalytics("Search")
    private val bottomTabsSwitcher by inject<BottomTabsSwitcher>()

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    @Composable
    override fun Screen() {
        MpeixTheme {
            val router = LocalRouter.current
            val keyboardController = LocalSoftwareKeyboardController.current
            ElmContent(
                key = "SearchScreen",
                storeFactory = {
                    dependencies.searchStoreFactory.create(
                        query = notNullStringArg(ARG_QUERY),
                        filter = notNullStringArg(ARG_FILTER),
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
                        bottomTabsSwitcher.changeTab(BottomTab.MAP)
                        keyboardController?.hide()
                        router.executeCommand(PopBackStack())
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
    }

    companion object {

        fun newInstance(query: String, filter: String = "ALL") = SearchFragment()
            .withArguments(
                ARG_QUERY to query,
                ARG_FILTER to filter,
            )
    }
}
