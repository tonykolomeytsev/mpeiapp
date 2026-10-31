package kekmech.ru.feature_onboarding_impl.presentation.screen

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_onboarding_impl.di.OnboardingDependencies
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.DASHBOARD
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

internal class WelcomeFragment : ComposeFragment() {

    private val dependencies by inject<OnboardingDependencies>()
    private val scheduleFeatureLauncher by fastLazy { dependencies.scheduleFeatureApi }

    @Composable
    override fun Screen() {
        MpeixTheme {
            val router = koinInject<Router>()
            WelcomeScreen(
                onContinueClick = {
                    router.executeCommand(AddScreenForward {
                        scheduleFeatureLauncher.getSearchGroupScreen(DASHBOARD, resultKey = "")
                    })
                }
            )
        }
    }
}
