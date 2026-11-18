package kekmech.ru.mpeiapp.robots

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import kekmech.ru.mpeiapp.MainActivity
import kekmech.ru.res_strings.R.string as Strings

/**
 * # DashboardRobot
 *
 * Dashboard tab of the main screen (see `DashboardScreen`)
 */
class DashboardRobot(compose: AndroidComposeTestRule<*, MainActivity>) : Robot(compose) {

    /**
     * Checks that the main screen is open on the dashboard tab
     */
    fun assertShown(): DashboardRobot = apply {
        waitForNode(bottomTab(Strings.navigation_name_dashboard) and isSelected())
    }

    /**
     * Checks the name of the selected schedule under the search field. Its item merges both texts,
     * so the name is checked on the item and not somewhere else on the screen
     */
    fun assertSelectedSchedule(name: String): DashboardRobot = apply {
        waitForNode(hasText(name) and hasText(string(Strings.dashboard_item_click_to_change_group)))
    }
}
