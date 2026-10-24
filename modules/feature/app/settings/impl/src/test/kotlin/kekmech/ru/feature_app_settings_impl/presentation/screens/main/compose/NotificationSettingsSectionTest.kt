package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import android.Manifest
import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import kekmech.ru.feature_schedule_api.ScheduleNotificationSettings
import kekmech.ru.feature_schedule_api.ScheduleNotificationSettings.State
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * The "Notifications" block of the settings: both kinds of notifications can be switched off and on.
 * Light / dark theme x phone / tablet widths. Runs on the JVM (Robolectric) with `./gradlew test`.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NotificationSettingsSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val app: Application get() = ApplicationProvider.getApplicationContext()

    private class FakeSettings(initial: State) : ScheduleNotificationSettings {
        val state = MutableStateFlow(initial)
        var detailsOpened = 0

        override fun current(): State = state.value
        override fun observe(): Flow<State> = state
        override fun setClassRemindersEnabled(enabled: Boolean) {
            state.value = state.value.copy(classReminders = enabled)
        }

        override fun setScheduleChangesEnabled(enabled: Boolean) {
            state.value = state.value.copy(scheduleChanges = enabled)
        }
    }

    @Before
    fun grantNotificationPermission() {
        // the permission dialog of Android 13+ is not part of this test
        shadowOf(app).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun show(settings: FakeSettings, darkTheme: Boolean, width: Int = 360) {
        composeRule.setContent {
            Box(Modifier.requiredSize(width.dp, 800.dp)) {
                MpeixTheme(darkTheme = darkTheme) {
                    NotificationSettingsSection(
                        settings = settings,
                        onOpenDetails = { settings.detailsOpened++ },
                    )
                }
            }
        }
    }

    private fun switchesCanBeTurnedOffAndOn(darkTheme: Boolean, width: Int) {
        val settings = FakeSettings(State(classReminders = true, scheduleChanges = false))
        show(settings, darkTheme, width)

        val switches = composeRule.onAllNodes(isToggleable())
        switches[0].assertIsOn()   // class reminders
        switches[1].assertIsOff()  // schedule changes

        // reminders: off, then on again
        switches[0].performClick()
        assertFalse(settings.current().classReminders)
        composeRule.onAllNodes(isToggleable())[0].assertIsOff()
        composeRule.onAllNodes(isToggleable())[0].performClick()
        assertTrue(settings.current().classReminders)

        // schedule changes: on, then off
        composeRule.onAllNodes(isToggleable())[1].performClick()
        assertTrue(settings.current().scheduleChanges)
        composeRule.onAllNodes(isToggleable())[1].assertIsOn()
        composeRule.onAllNodes(isToggleable())[1].performClick()
        assertFalse(settings.current().scheduleChanges)
    }

    @Test
    fun switches_light_phone() = switchesCanBeTurnedOffAndOn(darkTheme = false, width = 360)

    @Test
    fun switches_dark_phone() = switchesCanBeTurnedOffAndOn(darkTheme = true, width = 360)

    @Test
    fun switches_light_tablet() = switchesCanBeTurnedOffAndOn(darkTheme = false, width = 520)

    @Test
    fun switches_dark_tablet() = switchesCanBeTurnedOffAndOn(darkTheme = true, width = 520)

    @Test
    fun stateChangedElsewhere_isShownInTheSwitches() {
        val settings = FakeSettings(State(classReminders = true, scheduleChanges = true))
        show(settings, darkTheme = false)

        // e.g. the detailed notification screen switched the reminders off
        settings.setClassRemindersEnabled(false)
        composeRule.waitForIdle()

        composeRule.onAllNodes(isToggleable())[0].assertIsOff()
        composeRule.onAllNodes(isToggleable())[1].assertIsOn()
    }

    @Test
    fun detailsEntry_opensTheDetailedScreen() {
        val settings = FakeSettings(State(classReminders = true, scheduleChanges = true))
        show(settings, darkTheme = false)

        val title = app.getString(Strings.app_settings_notify_more_title)
        composeRule.onNodeWithText(title).performClick()

        assertEquals(1, settings.detailsOpened)
    }
}
