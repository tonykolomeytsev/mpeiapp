package kekmech.ru.feature_schedule

import androidx.fragment.app.Fragment
import kekmech.ru.common_android.withResultFor
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_schedule.find_schedule.FindScheduleFragment

class ScheduleFeatureLauncherImpl(
    private val router: Router
) : ScheduleFeatureLauncher {

    override fun launchSearchGroup(
        continueTo: String,
        targetFragment: Fragment?,
        requestCode: Int?
    ) {
        val findScheduleFragment = FindScheduleFragment.newInstance(continueTo)
        if (requestCode != null && targetFragment != null) {
            findScheduleFragment.withResultFor(targetFragment, requestCode)
        }
        router.executeCommand(AddScreenForward { findScheduleFragment })
    }
}