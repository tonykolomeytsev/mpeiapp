package kekmech.ru.feature_onboarding.launcher

import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.NewRoot
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.feature_onboarding.screens.WelcomeFragment

internal class OnboardingFeatureLauncherImpl(
    private val router: Router,
) : OnboardingFeatureLauncher {

    override fun launchWelcomePage(asNewRoot: Boolean) = router.executeCommand(
        if (asNewRoot) NewRoot { WelcomeFragment() } else AddScreenForward { WelcomeFragment() }
    )
}
