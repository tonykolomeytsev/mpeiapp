package kekmech.ru.feature_onboarding_impl.presentation.screen

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.feature_onboarding_impl.di.OnboardingDependencies
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.DASHBOARD
import org.koin.compose.koinInject

// Lets the switch finish its animation before the activity is recreated
private const val ACTIVITY_RECREATION_DELAY = 200L
private const val LANGUAGE_RESULT_KEY = "WELCOME_LANGUAGE_RESULT_KEY"

@Composable
internal fun WelcomeEntry() {
    val dependencies = koinInject<OnboardingDependencies>()
    val appSettingsRepository = dependencies.appSettingsRepository
    val activity = LocalActivity.current
    var appSettings by remember { mutableStateOf(appSettingsRepository.getAppSettings()) }

    ResultListener<String>(LANGUAGE_RESULT_KEY) { languageCode ->
        appSettings = appSettingsRepository.updateAppSettings { copy(languageCode = languageCode) }
        recreateActivity(activity)
    }

    WelcomeScreen(
        darkThemeEnabled = appSettings.isDarkThemeEnabled,
        languageCode = appSettings.languageCode,
        onToggleDarkTheme = { isEnabled ->
            if (isEnabled != appSettings.isDarkThemeEnabled) {
                appSettings = appSettingsRepository.updateAppSettings {
                    copy(isDarkThemeEnabled = isEnabled)
                }
                recreateActivity(activity)
            }
        },
        onClickLanguage = {
            dependencies.appSettingsFeatureLauncher.launchLanguageSelection(LANGUAGE_RESULT_KEY)
        },
        onContinueClick = {
            dependencies.scheduleFeatureApi.launchSearchGroup(DASHBOARD, resultKey = "")
        },
    )
}

private fun recreateActivity(activity: Activity?) {
    Handler(Looper.getMainLooper())
        .postDelayed({ activity?.recreate() }, ACTIVITY_RECREATION_DELAY)
}
