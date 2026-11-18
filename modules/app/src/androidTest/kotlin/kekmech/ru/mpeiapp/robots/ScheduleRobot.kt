package kekmech.ru.mpeiapp.robots

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import kekmech.ru.mpeiapp.MainActivity
import java.time.DayOfWeek
import kekmech.ru.res_strings.R.array as StringArrays
import kekmech.ru.res_strings.R.string as Strings

/**
 * # ScheduleRobot
 *
 * Schedule tab: the header with the week of the semester, the week calendar
 * and the classes of the selected day.
 */
class ScheduleRobot(compose: AndroidComposeTestRule<*, MainActivity>) : Robot(compose) {

    /**
     * Clicks the [day] in the week calendar
     */
    fun selectDay(day: DayOfWeek): ScheduleRobot = apply {
        // Indexed by DayOfWeek.value, as in ScheduleWeeksStrip: both the first and the last items are Sunday
        val label = stringArray(StringArrays.days_of_week_short)[day.value]
        click(hasText(label) and hasClickAction())
    }

    /**
     * Checks that the selected day has the classes [name] in the [place]. The card merges its texts,
     * so both are checked on the same card
     */
    fun assertClassesShown(name: String, place: String): ScheduleRobot = apply {
        waitForNode(hasText(name) and hasText(place))
    }

    fun assertWeekOfSemester(number: Int): ScheduleRobot = apply {
        waitForNode(hasText(string(Strings.schedule_semester_week, number)))
    }

    // No method for the week navigation button: it has no text or content description
    // in the semantics tree (the extended FAB clears the semantics of its text),
    // so it can't be found the way the user sees it
}
