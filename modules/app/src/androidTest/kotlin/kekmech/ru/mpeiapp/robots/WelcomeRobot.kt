package kekmech.ru.mpeiapp.robots

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import kekmech.ru.mpeiapp.MainActivity
import kekmech.ru.mpeiapp.testing.AppLanguage
import kekmech.ru.res_strings.R.string as Strings

/**
 * # WelcomeRobot
 *
 * First screen of the onboarding: the promo pager, the settings and the "Let's go" button
 * (see `WelcomeScreen`).
 */
class WelcomeRobot(compose: AndroidComposeTestRule<*, MainActivity>) : Robot(compose) {

    // Titles of the promo pages in the pager order, see PromoPage
    private val promoTitles = listOf(
        Strings.onboarding_welcome_title_schedule,
        Strings.onboarding_welcome_title_map,
        Strings.onboarding_welcome_title_bars,
        Strings.onboarding_welcome_title_notes,
    )

    // The only horizontally scrollable thing on the screen
    private val promoPager = SemanticsMatcher.keyIsDefined(SemanticsProperties.HorizontalScrollAxisRange)

    private val letsGoButton
        get() = kitButton(Strings.onboarding_welcome_start_using)

    fun assertShown(): WelcomeRobot = apply {
        waitForNode(letsGoButton)
    }

    /**
     * Swipes the promo pager from the first page to the last one, checking each page
     */
    fun swipePromoToTheEnd(): WelcomeRobot = apply {
        waitForNode(hasText(string(promoTitles.first())))
        promoTitles.drop(1).forEach { title ->
            waitForNode(promoPager).performTouchInput { swipeLeft() }
            waitForNode(hasText(string(title)))
        }
    }

    /**
     * Swipes the promo pager from the last page back to the first one, checking each page
     */
    fun swipePromoToTheStart(): WelcomeRobot = apply {
        waitForNode(hasText(string(promoTitles.last())))
        promoTitles.dropLast(1).reversed().forEach { title ->
            waitForNode(promoPager).performTouchInput { swipeRight() }
            waitForNode(hasText(string(title)))
        }
    }

    fun clickLetsGo(): FindScheduleRobot {
        click(letsGoButton)
        return FindScheduleRobot(compose).assertShown()
    }

    /**
     * Clicks the dark theme switch and waits for the app to recreate the activity with the new theme
     */
    fun toggleDarkTheme(): WelcomeRobot = apply {
        expectActivityRecreation { click(darkThemeSwitch()) }
    }

    /**
     * Checks both the switch and the theme the activity really shows
     */
    fun assertDarkTheme(isEnabled: Boolean): WelcomeRobot = apply {
        waitForNode(darkThemeSwitch() and if (isEnabled) isOn() else isOff())
        waitFor("The activity shows the ${if (isEnabled) "dark" else "light"} theme") {
            isNightMode() == isEnabled
        }
    }

    /**
     * Selects the [language] in the language selection and waits for the app to recreate
     * the activity in this language
     */
    fun selectLanguage(language: AppLanguage): WelcomeRobot = apply {
        click(hasText(string(Strings.app_settings_section_lang)) and hasClickAction())
        waitForNode(hasText(string(Strings.change_language_screen_title)))
        expectActivityRecreation { click(hasText(string(language.optionTitle)) and hasClickAction()) }
    }

    /**
     * Checks that the screen is in the [language]: the language setting shows it,
     * and the texts are in it
     */
    fun assertLanguage(language: AppLanguage): WelcomeRobot = apply {
        waitForNode(hasText(string(Strings.app_settings_section_lang, language.locale)) and hasText(language.shownName))
        val letsGo = string(Strings.onboarding_welcome_start_using, language.locale).uppercase()
        waitForNode(hasText(letsGo) and hasClickAction())
    }

    // The texts depend on the language, so the matcher is made each time
    private fun darkThemeSwitch(): SemanticsMatcher =
        hasText(string(Strings.app_settings_dark_theme)) and isToggleable()
}
