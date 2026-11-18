package kekmech.ru.mpeiapp.robots

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import kekmech.ru.mpeiapp.MainActivity
import kekmech.ru.res_strings.R.string as Strings

/**
 * # MainTabsRobot
 *
 * Bottom navigation bar of the main screen (see `MainTabsNavEntryDecorator`).
 * The main screen opens on the dashboard tab.
 */
class MainTabsRobot(compose: AndroidComposeTestRule<*, MainActivity>) : Robot(compose) {

    fun openSchedule(): ScheduleRobot {
        click(bottomTab(Strings.navigation_name_schedule))
        return ScheduleRobot(compose)
    }
}
