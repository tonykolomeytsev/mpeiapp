package kekmech.ru.feature_schedule

import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_schedule.find_schedule.FindScheduleFragment

class ScheduleFeatureLauncherImpl(
    private val router: Router
) : ScheduleFeatureLauncher {

    override fun launchSearchGroup() {
        router.executeCommand(AddScreenForward { FindScheduleFragment.newInstance() })
    }
}