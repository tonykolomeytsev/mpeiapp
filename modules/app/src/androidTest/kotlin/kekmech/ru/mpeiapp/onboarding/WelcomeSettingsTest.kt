package kekmech.ru.mpeiapp.onboarding

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kekmech.ru.mpeiapp.MainActivity
import kekmech.ru.mpeiapp.robots.WelcomeRobot
import kekmech.ru.mpeiapp.testing.AppLanguage
import kekmech.ru.mpeiapp.testing.AppSettingsRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

/**
 * Settings of the welcome screen: the theme and the language, each changed there and back.
 *
 * Preconditions: no app data and no selected schedule, the light theme and English. They are set
 * before the start, so the test is the same on a device in any language.
 */
@RunWith(AndroidJUnit4::class)
class WelcomeSettingsTest {

    private val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val rules: RuleChain = RuleChain
        .outerRule(AppSettingsRule(isDarkTheme = false, language = AppLanguage.ENGLISH))
        .around(compose)

    @Test
    fun switchThemeAndLanguageThereAndBack() {
        WelcomeRobot(compose)
            .assertShown()
            .assertDarkTheme(isEnabled = false)
            // Theme: dark, then light
            .toggleDarkTheme()
            .assertDarkTheme(isEnabled = true)
            .toggleDarkTheme()
            .assertDarkTheme(isEnabled = false)
            // Language: Russian, then English
            .assertLanguage(AppLanguage.ENGLISH)
            .selectLanguage(AppLanguage.RUSSIAN)
            .assertLanguage(AppLanguage.RUSSIAN)
            .selectLanguage(AppLanguage.ENGLISH)
            .assertLanguage(AppLanguage.ENGLISH)
    }
}
