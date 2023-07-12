package kekmech.ru.feature_schedule_impl.presentation.navigation

import kekmech.ru.feature_schedule_api.presentation.navigation.ScheduleSearchScreenApi
import kekmech.ru.feature_schedule_impl.presentation.screen.search.ScheduleSearchScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class ScheduleSearchScreenApiImpl : ScheduleSearchScreenApi {

    override fun navTarget(): NavTarget =
        ScheduleSearchScreenNavTarget()
}
