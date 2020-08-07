package kekmech.ru.feature_onboarding

import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.NewRoot
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.feature_onboarding.screens.BarsEntryFragment
import kekmech.ru.feature_onboarding.screens.WelcomeFragment

class OnboardingFeatureLauncherImpl(
    private val router: Router
) : OnboardingFeatureLauncher {

    override fun launchBarsPage() = router.executeCommand(AddScreenForward { BarsEntryFragment() })

    override fun launchWelcomePage(asNewRoot: Boolean) = router.executeCommand(
        if (asNewRoot) NewRoot { WelcomeFragment() } else AddScreenForward { WelcomeFragment() }
    )
}