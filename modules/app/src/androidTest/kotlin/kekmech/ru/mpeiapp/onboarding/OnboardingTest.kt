package kekmech.ru.mpeiapp.onboarding

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kekmech.ru.mpeiapp.MainActivity
import kekmech.ru.mpeiapp.robots.WelcomeRobot
import kekmech.ru.mpeiapp.testing.MockBackend
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration.Companion.seconds

/**
 * Onboarding of a freshly installed app, on the mock backend.
 *
 * Preconditions: the app has no data and no selected schedule. The runner wipes the app data
 * before every test, and these tests don't select a schedule.
 *
 * "No crashes" needs no check of its own: a crash of the app fails the test.
 */
@RunWith(AndroidJUnit4::class)
class OnboardingTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun selectGroupAndOpenDashboard() {
        selectScheduleAndOpenDashboard(partialName = "Сэ-12", schedule = MockBackend.GROUP)
    }

    @Test
    fun selectTeacherAndOpenDashboard() {
        selectScheduleAndOpenDashboard(partialName = "Куликова", schedule = MockBackend.TEACHER)
    }

    /**
     * The whole case, the same for a group and a teacher: only the search differs.
     * The search by the [partialName] has to suggest the [schedule]
     */
    private fun selectScheduleAndOpenDashboard(partialName: String, schedule: String) {
        WelcomeRobot(compose)
            .assertShown()
            .swipePromoToTheEnd()
            .swipePromoToTheStart()
            .clickLetsGo()
            .assertKeyboardShown()
            // Invalid input: one digit
            .typeText("1")
            .assertQuery("1")
            .wait(1.seconds)
            .deleteLastCharacter()
            .assertQuery("")
            // Invalid input: one letter
            .typeText("С")
            .assertQuery("С")
            .wait(1.seconds)
            .deleteLastCharacter()
            .assertQuery("")
            .clickToolbarBack()
            .clickLetsGo()
            .assertKeyboardShown()
            .leaveWithSystemBack()
            .clickLetsGo()
            .assertKeyboardShown()
            // A valid name, partially
            .typeText(partialName)
            .selectSuggestion(schedule)
            .assertQuery(schedule)
            .clickContinue()
            .assertShown()
            .assertSelectedSchedule(schedule)
    }
}
