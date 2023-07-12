package kekmech.ru.feature_onboarding_impl.presentation.navigation

import kekmech.ru.feature_onboarding_api.presentation.navigation.OnboardingScreenApi
import kekmech.ru.feature_onboarding_impl.presentation.screen.WelcomeScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class OnboardingScreenApiImpl : OnboardingScreenApi {

    override fun navTarget(): NavTarget =
        WelcomeScreenNavTarget()
}
