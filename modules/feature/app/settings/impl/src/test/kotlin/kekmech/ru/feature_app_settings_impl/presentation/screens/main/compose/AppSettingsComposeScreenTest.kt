package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEffect
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsState
import kekmech.ru.feature_schedule_api.ScheduleNotificationSettings
import kekmech.ru.feature_schedule_api.ScheduleNotificationSettings.State
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * The whole "Profile" tab (the settings screen) is composed: light / dark x phone / tablet.
 * An exception thrown while composing it would leave the tab empty or broken on a device.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppSettingsComposeScreenTest {

    @get:org.junit.Rule
    val composeRule = createComposeRule()

    private val app: Application get() = ApplicationProvider.getApplicationContext()

    private class FakeSettings : ScheduleNotificationSettings {
        private val state = MutableStateFlow(State(classReminders = true, scheduleChanges = true))
        override fun current(): State = state.value
        override fun observe(): Flow<State> = state
        override fun setClassRemindersEnabled(enabled: Boolean) = Unit
        override fun setScheduleChangesEnabled(enabled: Boolean) = Unit
        override fun setLockScreenScheduleEnabled(enabled: Boolean) = Unit
    }

    private var openedProfile: String? = null

    private fun show(darkTheme: Boolean, width: Int) {
        composeRule.setContent {
            Box(Modifier.requiredSize(width.dp, 800.dp)) {
                MpeixTheme(darkTheme = darkTheme) {
                    AppSettingsComposeScreen(
                        state = AppSettingsState(
                            appSettings = AppSettings(
                                isDarkThemeEnabled = darkTheme,
                                appTheme = AppTheme.System,
                                isSnowEnabled = false,
                                languageCode = "ru_RU",
                                showNavigationButton = true,
                                autoHideBottomSheet = true,
                                mapAppearanceType = "scheme",
                            ),
                            isFeatureToggleSnowFlakesEnabled = false,
                            appVersionName = AppVersionName("v0.5.0"),
                        ),
                        effects = emptyFlow<AppSettingsEffect>(),
                        onAccept = {},
                        onOpenFavorites = {},
                        onOpenScheduleNotifications = {},
                        notificationSettings = FakeSettings(),
                        onOpenGithub = {},
                        onOpenContributor = { openedProfile = it },
                        onLogoutBars = {},
                        onOpenLanguageSelection = {},
                        onOpenMapTypeSelection = {},
                        onRecreateActivity = {},
                    )
                }
            }
        }
    }

    private fun scrollTo(text: String) {
        composeRule.onNode(hasScrollAction()).performScrollToNode(hasText(text))
    }

    private fun screenIsComposedAndShowsTheTeam(darkTheme: Boolean, width: Int) {
        show(darkTheme, width)

        for (name in listOf(
            Strings.developer_rasul_rustamov_name,
            Strings.developer_ivan_myalitsin_name,
            Strings.developer_johnny_wickky_name,
        )) {
            val text = app.getString(name)
            scrollTo(text)
            composeRule.onNodeWithText(text).assertIsDisplayed()
        }
        composeRule.onNodeWithText(app.getString(Strings.app_settings_header_developers)).assertIsDisplayed()

        // the version has exactly one "v"
        val version = app.getString(Strings.app_settings_app_version, "0.5.0")
        scrollTo(version)
        composeRule.onNodeWithText(version).assertIsDisplayed()
        assertFalse(version, version.contains("vv"))
    }

    @Test
    fun profile_light_phone() = screenIsComposedAndShowsTheTeam(darkTheme = false, width = 360)

    @Test
    fun profile_dark_phone() = screenIsComposedAndShowsTheTeam(darkTheme = true, width = 360)

    @Test
    fun profile_light_tablet() = screenIsComposedAndShowsTheTeam(darkTheme = false, width = 520)

    @Test
    fun profile_dark_tablet() = screenIsComposedAndShowsTheTeam(darkTheme = true, width = 520)

    @Test
    fun developerCard_opensTheProfile() {
        show(darkTheme = false, width = 360)
        val ivan = app.getString(Strings.developer_ivan_myalitsin_name)
        scrollTo(ivan)
        composeRule.onNodeWithText(ivan).performClick()
        assertEquals("https://github.com/bezpaniki-arch", openedProfile)
    }
}
