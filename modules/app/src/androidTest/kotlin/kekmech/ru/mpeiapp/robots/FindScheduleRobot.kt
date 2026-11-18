package kekmech.ru.mpeiapp.robots

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import kekmech.ru.mpeiapp.MainActivity
import kotlin.time.Duration
import kekmech.ru.res_strings.R.string as Strings

/**
 * # FindScheduleRobot
 *
 * Search of the schedule by the group number or the teacher name (see `FindScheduleScreen`):
 * the search field, the suggestions under it and the "Continue" button.
 */
class FindScheduleRobot(compose: AndroidComposeTestRule<*, MainActivity>) : Robot(compose) {

    // The only text field on the screen
    private val searchField = hasSetTextAction()

    // The arrow has neither a text nor a content description (BackIconButton of ui_kit_topappbar),
    // so it is found as a button without a label. The clear button of the search field looks the same,
    // it is shown only while the field has text
    private val toolbarBackArrow =
        SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button) and
            SemanticsMatcher.keyNotDefined(SemanticsProperties.Text) and
            SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentDescription)

    fun assertShown(): FindScheduleRobot = apply {
        waitForNode(hasText(string(Strings.schedule_find_header)))
    }

    /**
     * Checks that the search field has the focus and the on-screen keyboard is shown.
     * A device with a hardware keyboard may not show the on-screen one
     */
    fun assertKeyboardShown(): FindScheduleRobot = apply {
        waitForNode(searchField and isFocused())
        waitFor("The on-screen keyboard is shown") { isKeyboardShown() }
    }

    fun typeText(text: String): FindScheduleRobot = apply {
        waitForNode(searchField).performTextInput(text)
    }

    /**
     * Presses Backspace in the search field
     */
    @OptIn(ExperimentalTestApi::class)
    fun deleteLastCharacter(): FindScheduleRobot = apply {
        waitForNode(searchField).performKeyInput { pressKey(Key.Backspace) }
    }

    fun assertQuery(query: String): FindScheduleRobot = apply {
        waitForNode(searchField and hasQuery(query))
    }

    /**
     * Does nothing for the [duration], as the user waits in the test case
     */
    fun wait(duration: Duration): FindScheduleRobot = apply {
        pause(duration)
    }

    fun selectSuggestion(name: String): FindScheduleRobot = apply {
        // The search field is clickable too and may have the same text
        click(hasText(name) and hasClickAction() and !searchField)
    }

    /**
     * Clicks the arrow in the toolbar. The search field must be empty, see [toolbarBackArrow]
     */
    fun clickToolbarBack(): WelcomeRobot {
        click(toolbarBackArrow)
        return WelcomeRobot(compose).assertShown()
    }

    /**
     * Leaves the screen with the system BACK button.
     * While the keyboard is open, the first press only hides it, as it does for the user
     */
    fun leaveWithSystemBack(): WelcomeRobot {
        if (isKeyboardShown()) {
            pressSystemBack()
            waitFor("The on-screen keyboard is hidden") { !isKeyboardShown() }
        }
        pressSystemBack()
        return WelcomeRobot(compose).assertShown()
    }

    fun clickContinue(): DashboardRobot {
        click(kitButton(Strings.schedule_find_button_continue))
        return DashboardRobot(compose)
    }

    private fun hasQuery(query: String): SemanticsMatcher =
        SemanticsMatcher("search query is '$query'") { node ->
            node.config.getOrNull(SemanticsProperties.EditableText)?.text == query
        }
}
