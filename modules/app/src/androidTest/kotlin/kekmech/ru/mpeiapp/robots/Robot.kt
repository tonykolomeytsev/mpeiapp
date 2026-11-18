package kekmech.ru.mpeiapp.robots

import android.content.res.Configuration
import android.os.SystemClock
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.espresso.Espresso
import kekmech.ru.mpeiapp.MainActivity
import java.util.Locale
import kotlin.time.Duration

/**
 * # Robot
 *
 * Base of the screen robots. A robot knows how to find things on its screen, the tests only say
 * what the user does and sees. When the UI changes, the robot is fixed, not every test.
 *
 * The nodes are found by the texts the user sees. The texts are taken from the resources
 * of the activity, so they are in the language the app shows.
 *
 * The mock backend answers with a random delay, so every lookup waits for the node to appear.
 */
abstract class Robot(protected val compose: AndroidComposeTestRule<*, MainActivity>) {

    protected fun string(@StringRes id: Int, vararg args: Any): String =
        compose.activity.getString(id, *args)

    /**
     * The text with the [id] in the [locale], whatever language the app shows now
     */
    protected fun string(@StringRes id: Int, locale: Locale): String {
        val activity = compose.activity
        val configuration = Configuration(activity.resources.configuration).apply { setLocale(locale) }
        return activity.createConfigurationContext(configuration).getString(id)
    }

    protected fun stringArray(@ArrayRes id: Int): Array<String> =
        compose.activity.resources.getStringArray(id)

    /**
     * Waits until a node matching the [matcher] is on the screen and returns it.
     * The nodes out of the screen, e.g. the neighbour pages of a pager, are skipped.
     */
    protected fun waitForNode(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = DefaultTimeoutMillis,
    ): SemanticsNodeInteraction {
        try {
            compose.waitUntil(timeoutMillis) { findDisplayedNode(matcher) != null }
        } catch (e: ComposeTimeoutException) {
            // The tree of what is on the screen instead, to see why the node didn't match
            val screen = compose.onAllNodes(isRoot()).printToString(maxDepth = Int.MAX_VALUE)
            throw AssertionError(
                "No node ${matcher.description} on the screen after $timeoutMillis ms. The screen:\n$screen",
                e,
            )
        }
        return checkNotNull(findDisplayedNode(matcher))
    }

    /**
     * Waits until the [condition] described by the [description] is true
     */
    protected fun waitFor(
        description: String,
        timeoutMillis: Long = DefaultTimeoutMillis,
        condition: () -> Boolean,
    ) {
        try {
            compose.waitUntil(timeoutMillis, condition)
        } catch (e: ComposeTimeoutException) {
            throw AssertionError("Not true after $timeoutMillis ms: $description", e)
        }
    }

    protected fun click(matcher: SemanticsMatcher) {
        waitForNode(matcher).performClick()
    }

    /**
     * Button of ui_kit_buttons with the [text]: it shows the text in upper case
     */
    protected fun kitButton(@StringRes text: Int): SemanticsMatcher =
        hasText(string(text).uppercase()) and hasClickAction()

    /**
     * Tab of the bottom navigation bar with the [label] (see `MainTabsNavEntryDecorator`)
     */
    protected fun bottomTab(@StringRes label: Int): SemanticsMatcher =
        hasText(string(label)) and SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Tab)

    /**
     * Presses the system BACK button, the back gesture does the same
     */
    protected fun pressSystemBack() {
        Espresso.pressBack()
    }

    /**
     * Whether the on-screen keyboard is shown over the app
     */
    protected fun isKeyboardShown(): Boolean {
        val decorView = compose.activity.window.decorView
        return compose.runOnUiThread {
            ViewCompat.getRootWindowInsets(decorView)?.isVisible(WindowInsetsCompat.Type.ime()) == true
        }
    }

    /**
     * Whether the activity shows the dark theme: the app overrides the night mode of the activity
     * with its own setting (see `NightModeContextWrapper`)
     */
    protected fun isNightMode(): Boolean =
        compose.activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
            Configuration.UI_MODE_NIGHT_YES

    /**
     * Runs the [action] after which the app recreates the activity (e.g. a new theme or language)
     * and waits for the new activity
     */
    protected fun expectActivityRecreation(action: () -> Unit) {
        val oldActivity = compose.activity
        action()
        waitFor("The activity is recreated") {
            // There is no activity to get for a moment, while the old one is destroyed
            val activity = runCatching { compose.activity }.getOrNull()
            activity != null && activity !== oldActivity
        }
    }

    /**
     * Waits for the [duration] with the app running, as the user does in the test case.
     * Not for synchronization: to wait for something to appear, use [waitForNode]
     */
    protected fun pause(duration: Duration) {
        SystemClock.sleep(duration.inWholeMilliseconds)
    }

    private fun findDisplayedNode(matcher: SemanticsMatcher): SemanticsNodeInteraction? {
        val nodes = compose.onAllNodes(matcher)
        val count = nodes.fetchSemanticsNodes().size
        // A node may leave the composition between the lookup and the check
        return List(count) { nodes[it] }.firstOrNull { runCatching { it.isDisplayed() }.getOrDefault(false) }
    }

    private companion object {

        const val DefaultTimeoutMillis = 5_000L
    }
}
