package kekmech.ru.domain_schedule

const val CONTINUE_TO_DASHBOARD = "Dashboard"
const val CONTINUE_TO_BARS_ONBOARDING = "Bars"
const val CONTINUE_TO_BACK_STACK = "Back"

interface ScheduleFeatureLauncher {
    fun launchSearchGroup(continueTo: String = CONTINUE_TO_BACK_STACK)
}