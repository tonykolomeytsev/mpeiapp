package kekmech.ru.feature_schedule_impl.presentation.navigation

import kekmech.ru.feature_schedule_api.presentation.navigation.ScheduleScreenApi
import kekmech.ru.feature_schedule_impl.presentation.screen.main.ScheduleScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class ScheduleScreenApiImpl : ScheduleScreenApi {

    override fun navTarget(): NavTarget =
        ScheduleScreenNavTarget()
}
