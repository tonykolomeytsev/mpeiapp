package kekmech.ru.feature_onboarding_impl.presentation.navigation

import kekmech.ru.feature_onboarding_api.OnboardingFeatureLauncher
import kekmech.ru.feature_onboarding_impl.presentation.screen.WelcomeFragment
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.NewRoot
import kekmech.ru.lib_navigation.Router

internal class OnboardingFeatureLauncherImpl(
    private val router: Router,
) : OnboardingFeatureLauncher {

    override fun launchWelcomePage(asNewRoot: Boolean) = router.executeCommand(
        if (asNewRoot) NewRoot { WelcomeFragment() } else AddScreenForward { WelcomeFragment() }
    )
}
