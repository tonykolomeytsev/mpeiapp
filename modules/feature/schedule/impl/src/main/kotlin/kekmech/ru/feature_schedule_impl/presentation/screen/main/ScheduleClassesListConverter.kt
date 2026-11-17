package kekmech.ru.feature_schedule_impl.presentation.screen.main

import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayPage
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayPagesConverter

internal object ScheduleClassesListConverter {

    /**
     * Pages of the selected week and its neighbours by their week offsets: the endless days pager
     * shows Saturday of the previous week and Monday of the next one while swiping past the selected week
     */
    fun map(state: ScheduleState): Map<Int, List<ScheduleDayPage>> =
        (state.weekOffset - 1..state.weekOffset + 1).associateWith { weekOffset ->
            val resource = state.schedule[weekOffset]
            ScheduleDayPagesConverter.map(
                schedule = resource?.value,
                error = state.observeErrors[weekOffset] ?: resource?.error,
            )
        }
}
