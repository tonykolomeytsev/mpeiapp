package kekmech.ru.feature_onboarding_impl.di

import kekmech.ru.feature_onboarding_api.OnboardingFeatureLauncher
import kekmech.ru.feature_onboarding_api.presentation.navigation.OnboardingScreenApi
import kekmech.ru.feature_onboarding_impl.presentation.navigation.OnboardingFeatureLauncherImpl
import kekmech.ru.feature_onboarding_impl.presentation.navigation.OnboardingScreenApiImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureOnboardingModule = module {
    factoryOf(::OnboardingDependencies)
    factoryOf(::OnboardingFeatureLauncherImpl) bind OnboardingFeatureLauncher::class
    factoryOf(::OnboardingScreenApiImpl) bind OnboardingScreenApi::class
}
