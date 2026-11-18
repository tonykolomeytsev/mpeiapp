package kekmech.ru.mpeiapp.testing

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.ExternalResource

/**
 * # AppSettingsRule
 *
 * Sets the theme and the language of the app before it starts, so the test doesn't depend on
 * the settings of the device: without them the app follows the device language.
 *
 * `MainActivity` applies them when it is created, so put this rule before the compose rule
 * in a `RuleChain`, as `SelectedScheduleRule`.
 */
class AppSettingsRule(
    private val isDarkTheme: Boolean,
    private val language: AppLanguage,
) : ExternalResource() {

    override fun before() {
        InstrumentationRegistry.getInstrumentation().targetContext.editAppPreferences {
            putBoolean("app_is_dark_theme_enabled", isDarkTheme)
            putString("app_lang", language.code)
        }
    }
}
