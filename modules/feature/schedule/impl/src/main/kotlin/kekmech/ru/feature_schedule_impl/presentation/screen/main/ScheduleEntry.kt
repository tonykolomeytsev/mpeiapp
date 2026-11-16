package kekmech.ru.feature_schedule_impl.presentation.screen.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation3.runtime.result.ResultEffect
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.feature_schedule_impl.di.ScheduleDependencies
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDaysAction
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEffect
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent.Ui
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleStoreFactory
import kekmech.ru.lib_analytics_android.Analytics
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import org.koin.compose.koinInject

private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
private const val LUNCH_MAP_DEEPLINK = "mpeix://map?tab=FOOD"

@Composable
internal fun ScheduleEntry() {
    val analytics = rememberScreenAnalytics("Schedule")
    val dependencies = koinInject<ScheduleDependencies>()
    val storeFactory = koinInject<ScheduleStoreFactory>()
    val context = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity
    // The store lives with the activity: the screen is recreated on every return to the tab,
    // but the selected week and day stay where the user left them
    ElmContent(
        key = "ScheduleScreen",
        storeFactory = { storeFactory.create() },
        viewModelStoreOwner = activity,
        savedStateRegistryOwner = activity,
    ) { onAccept, state, effects ->
        // Notes of the classes could change in the note list
        ResultEffect<Unit>(NOTES_LIST_RESULT_KEY) { onAccept(Ui.Action.NotesUpdated) }
        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { onAccept(Ui.Action.UpdateScheduleIfNeeded) }

        EffectHandler(effects) { effect ->
            when (effect) {
                is ScheduleEffect.NavigateToNoteList -> dependencies.notesFeatureLauncher.launchNoteList(
                    selectedClasses = effect.classes,
                    selectedDate = effect.date,
                    resultKey = NOTES_LIST_RESULT_KEY,
                )
            }
        }

        ScheduleScreen(
            ui = remember(state) { ScheduleUiConverter.map(state) },
            onAction = { action ->
                when (action) {
                    is ScheduleAction.WeekSelected -> {
                        analytics.sendClick("Week_${action.weekOffset}")
                        onAccept(Ui.Action.SelectWeek(action.weekOffset))
                    }

                    is ScheduleAction.WeeksDragged -> analytics.sendScroll("WeeksRecyclerView")
                    is ScheduleAction.DayClick -> {
                        analytics.sendClick("Day_${action.date}")
                        onAccept(Ui.Click.Day(action.date))
                    }

                    is ScheduleAction.FabClick -> onAccept(Ui.Click.FAB)
                    is ScheduleAction.Days -> handleDaysAction(action.action, onAccept, analytics) {
                        context.openLinkExternal(LUNCH_MAP_DEEPLINK)
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
        is ScheduleDaysAction.PageSelected -> onAccept(Ui.Action.PageChanged(action.page))
        is ScheduleDaysAction.PagerDragged -> analytics.sendScroll("WorkingDaysViewPager")
        is ScheduleDaysAction.ClassesScrolled -> onAccept(Ui.Action.ClassesScrolled(action.dy))
        is ScheduleDaysAction.ClassesClick -> {
            analytics.sendClick("ClickClasses")
            onAccept(Ui.Click.Classes(action.classes))
        }

        is ScheduleDaysAction.LunchClick -> onLunchClick()
        is ScheduleDaysAction.SavedScheduleBannerClick -> {
            analytics.sendClick("SavedScheduleBannerRefresh")
            onAccept(Ui.Click.SavedScheduleBanner)
        }

        is ScheduleDaysAction.ReloadClick -> {
            analytics.sendClick("ClassesReload")
            onAccept(Ui.Click.Reload)
        }
    }
}
