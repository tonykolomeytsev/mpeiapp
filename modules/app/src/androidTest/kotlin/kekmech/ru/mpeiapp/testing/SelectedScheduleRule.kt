package kekmech.ru.mpeiapp.testing

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.ExternalResource

/**
 * # SelectedScheduleRule
 *
 * Selects the schedule with the [name] before the app starts, so it opens the main screen
 * instead of the onboarding. The type of the schedule is derived from the name, as the app does
 * (see `SelectedScheduleSource`).
 *
 * `MainActivity` reads the selected schedule in `onCreate`, so put this rule before the compose rule:
 * ```kotlin
 * @get:Rule
 * val rules: RuleChain = RuleChain
 *     .outerRule(SelectedScheduleRule(MockBackend.GROUP))
 *     .around(compose)
 * ```
 */
class SelectedScheduleRule(private val name: String) : ExternalResource() {

    override fun before() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .editAppPreferences { putString("selected_group", name) }
    }
}
