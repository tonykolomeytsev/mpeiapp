package kekmech.ru.feature_onboarding_impl.presentation.navigation

import androidx.fragment.app.Fragment
import kekmech.ru.feature_onboarding_api.OnboardingFeatureApi
import kekmech.ru.feature_onboarding_impl.presentation.screen.WelcomeFragment
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.NewRoot
import kekmech.ru.lib_navigation.Router

internal class OnboardingFeatureApiImpl : OnboardingFeatureApi {

    override fun getScreen(): Fragment = WelcomeFragment()
}
