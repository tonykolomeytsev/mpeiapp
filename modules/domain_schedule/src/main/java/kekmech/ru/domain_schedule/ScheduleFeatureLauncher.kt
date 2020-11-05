package kekmech.ru.domain_schedule

import androidx.fragment.app.Fragment

const val CONTINUE_TO_DASHBOARD = "Dashboard"
const val CONTINUE_TO_BARS_ONBOARDING = "Bars"
const val CONTINUE_TO_BACK_STACK = "Back"
const val CONTINUE_TO_BACK_STACK_WITH_RESULT = "WithResult"

interface ScheduleFeatureLauncher {

    fun launchMain(): Fragment

    fun launchSearchGroup(
        continueTo: String = CONTINUE_TO_BACK_STACK,
        targetFragment: Fragment? = null,
        requestCode: Int? = null,
        selectGroupAfterSuccess: Boolean = true
    )
}