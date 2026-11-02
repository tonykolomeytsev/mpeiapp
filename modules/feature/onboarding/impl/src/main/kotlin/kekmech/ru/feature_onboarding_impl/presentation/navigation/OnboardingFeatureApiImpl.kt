package kekmech.ru.feature_onboarding_impl.presentation.navigation

import kekmech.ru.feature_onboarding_api.OnboardingFeatureApi
import kekmech.ru.lib_navigation.NewRoot
import kekmech.ru.lib_navigation.Router

internal class OnboardingFeatureApiImpl(
    private val router: Router,
) : OnboardingFeatureApi {

    override fun launch() {
        router.executeCommand(NewRoot(WelcomeScreenKey))
    }
}
