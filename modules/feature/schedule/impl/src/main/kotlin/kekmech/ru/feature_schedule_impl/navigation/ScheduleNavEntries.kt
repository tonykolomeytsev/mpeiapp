package kekmech.ru.feature_schedule_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.FindScheduleEntry
import kekmech.ru.feature_schedule_impl.presentation.screen.main.ScheduleEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScheduleTabKey
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.tabEntry

internal class ScheduleNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        tabEntry(ScheduleTabKey) { ScheduleEntry() }
        entry<FindScheduleScreenKey> { FindScheduleEntry(it) }
    }
}
