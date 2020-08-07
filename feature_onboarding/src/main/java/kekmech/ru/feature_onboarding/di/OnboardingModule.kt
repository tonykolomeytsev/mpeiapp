package kekmech.ru.feature_onboarding.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.feature_onboarding.OnboardingFeatureLauncherImpl
import org.koin.dsl.bind

object OnboardingModule : ModuleProvider({
    single { OnboardingDependencies(get()) } bind OnboardingDependencies::class
    single { OnboardingFeatureLauncherImpl(get()) } bind OnboardingFeatureLauncher::class
})