package kekmech.ru.mpeiapp.schedule

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kekmech.ru.mpeiapp.MainActivity
import kekmech.ru.mpeiapp.robots.MainTabsRobot
import kekmech.ru.mpeiapp.testing.MockBackend
import kekmech.ru.mpeiapp.testing.SelectedScheduleRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import java.time.DayOfWeek

/**
 * Smoke test of the schedule tab with a selected group, on the mock backend
 */
@RunWith(AndroidJUnit4::class)
class ScheduleSmokeTest {

    private val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val rules: RuleChain = RuleChain
        .outerRule(SelectedScheduleRule(MockBackend.GROUP))
        .around(compose)

    @Test
    fun tuesdayShowsItsClasses() {
        // Tuesday is the same in the even and the odd weeks of the mock,
        // so it doesn't matter which week the schedule opens
        MainTabsRobot(compose)
            .openSchedule()
            .selectDay(DayOfWeek.TUESDAY)
            .assertClassesShown(name = "Иностранный язык", place = "М-812")
            .assertClassesShown(name = "Механика материалов и конструкций", place = "Б-413")
    }
}
