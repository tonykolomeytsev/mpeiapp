package kekmech.ru.feature_dashboard_impl.presentation.screen.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_dashboard_impl.di.DashboardDependencies
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEffect
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent.Ui
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardStoreFactory
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.ui_kit_banner.BannerHost
import kekmech.ru.ui_kit_banner.BannerHostState
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject
import kekmech.ru.res_strings.R.string as Strings

private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
private const val EDIT_NOTE_RESULT_KEY = "EDIT_NOTE_RESULT_KEY"
private const val FIND_GROUP_RESULT_KEY = "FIND_GROUP_RESULT_KEY"

@Composable
internal fun DashboardEntry() {
    val analytics = rememberScreenAnalytics("Dashboard")
    val dependencies = koinInject<DashboardDependencies>()
    val context = LocalContext.current
    val bannerHostState = remember { BannerHostState() }
    val scrollToTopRequests = remember(dependencies) {
        dependencies.bottomTabsSwitcher.reselects
            .filter { it == BottomTab.DASHBOARD }
            .map { }
    }
    val activity = LocalActivity.current as ComponentActivity
    MpeixTheme {
        val storeFactory = koinInject<DashboardStoreFactory>()
        ElmContent(
            key = "DashboardScreen",
            storeFactory = { storeFactory.create() },
            viewModelStoreOwner = activity,
            savedStateRegistryOwner = activity,
        ) { onAccept, state, effects ->
            // Results of the screens opened from the dashboard: notes or selected schedule could change
            ResultListener<EmptyResult>(NOTES_LIST_RESULT_KEY) { onAccept(Ui.Action.SwipeToRefresh) }
            ResultListener<EmptyResult>(EDIT_NOTE_RESULT_KEY) { onAccept(Ui.Action.SwipeToRefresh) }
            ResultListener<SelectedSchedule>(FIND_GROUP_RESULT_KEY) { onAccept(Ui.Action.SwipeToRefresh) }

            EffectHandler(effects) { effect ->
                when (effect) {
                    is DashboardEffect.ShowLoadingError ->
                        bannerHostState.show(context.getString(Strings.dashboard_loading_error))

                    is DashboardEffect.ShowNotesLoadingError ->
                        bannerHostState.show(context.getString(Strings.something_went_wrong_error))

                    is DashboardEffect.NavigateToNotesList ->
                        dependencies.notesFeatureLauncher.launchNoteList(
                            selectedClasses = effect.classes,
                            selectedDate = effect.date,
                            resultKey = NOTES_LIST_RESULT_KEY,
                        )
                }
            }

            Box {
                DashboardScreen(
                    state = state,
                    scrollToTopRequests = scrollToTopRequests,
                    onAction = { action ->
                        when (action) {
                            is DashboardAction.Refresh -> onAccept(Ui.Action.SwipeToRefresh)
                            is DashboardAction.Reload -> {
                                analytics.sendClick("DashboardReload")
                                onAccept(Ui.Action.SwipeToRefresh)
                            }

                            is DashboardAction.SearchClick -> {
                                analytics.sendClick("Search")
                                dependencies.searchFeatureLauncher.launch()
                            }

                            is DashboardAction.ChangeScheduleClick -> {
                                analytics.sendClick("ChangeGroup")
                                dependencies.scheduleFeatureApi.launchSearchGroup(
                                    continueTo = ScheduleFeatureApi.ContinueTo.BACK_WITH_RESULT,
                                    resultKey = FIND_GROUP_RESULT_KEY,
                                )
                            }

                            is DashboardAction.LunchBannerClick -> {
                                analytics.sendClick("BannerLunch")
                                dependencies.bottomTabsSwitcher.changeTab(BottomTab.MAP)
                            }

                            is DashboardAction.ShowAllNotesClick -> {
                                analytics.sendClick("ShowAllNotes")
                                dependencies.notesFeatureLauncher.launchAllNotes()
                            }

                            is DashboardAction.SetUpFavoritesClick -> {
                                analytics.sendClick("SetUpFavorites")
                                dependencies.appSettingsFeatureLauncher.launch(
                                    AppSettingsFeatureLauncher.SubPage.FAVORITES
                                )
                            }

                            is DashboardAction.ListScrolled -> analytics.sendScroll("DashboardRecyclerView")
                            is DashboardAction.ClassesClick -> {
                                analytics.sendClick("ClickClasses")
                                onAccept(Ui.Click.ClassesItem(action.classes))
                            }

                            is DashboardAction.NoteClick -> {
                                analytics.sendClick("EditNote")
                                dependencies.notesFeatureLauncher.launchNoteEdit(
                                    note = action.note,
                                    resultKey = EDIT_NOTE_RESULT_KEY,
                                )
                            }

                            is DashboardAction.FavoriteScheduleClick -> {
                                analytics.sendClick("FavoriteSchedule")
                                onAccept(Ui.Action.SelectFavoriteSchedule(action.favoriteSchedule))
                            }
                        }
                    },
                )
                BannerHost(bannerHostState)
            }
        }
    }
}
