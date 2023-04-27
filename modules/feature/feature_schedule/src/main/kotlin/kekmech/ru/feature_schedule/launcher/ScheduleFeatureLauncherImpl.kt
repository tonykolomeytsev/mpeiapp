package kekmech.ru.feature_schedule.launcher

import androidx.fragment.app.Fragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_schedule.screens.find_schedule.FindScheduleFragment
import kekmech.ru.feature_schedule.screens.main.ScheduleFragment

internal class ScheduleFeatureLauncherImpl(
    private val router: Router,
) : ScheduleFeatureLauncher {

    override fun getScreen(): Fragment = ScheduleFragment()

    override fun launchSearchGroup(
        continueTo: ScheduleFeatureLauncher.ContinueTo,
        selectGroupAfterSuccess: Boolean,
        resultKey: String,
    ) {
        router.executeCommand(
            AddScreenForward {
                FindScheduleFragment.newInstance(continueTo, selectGroupAfterSuccess, resultKey)
            }
        )
    }
}
