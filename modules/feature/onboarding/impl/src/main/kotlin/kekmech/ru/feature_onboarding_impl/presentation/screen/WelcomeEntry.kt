package kekmech.ru.feature_onboarding_impl.presentation.screen

import androidx.compose.runtime.Composable
import kekmech.ru.feature_onboarding_impl.di.OnboardingDependencies
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.DASHBOARD
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

@Composable
internal fun WelcomeEntry() {
    val dependencies = koinInject<OnboardingDependencies>()
    MpeixTheme {
        WelcomeScreen(
            onContinueClick = {
                dependencies.scheduleFeatureApi.launchSearchGroup(DASHBOARD, resultKey = "")
            }
        )
    }
}
