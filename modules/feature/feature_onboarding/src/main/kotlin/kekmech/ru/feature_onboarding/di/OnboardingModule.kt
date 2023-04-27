package kekmech.ru.feature_onboarding.di

import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.feature_onboarding.launcher.OnboardingFeatureLauncherImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureOnboardingModule = module {
    factoryOf(::OnboardingDependencies)
    factoryOf(::OnboardingFeatureLauncherImpl) bind OnboardingFeatureLauncher::class
}
