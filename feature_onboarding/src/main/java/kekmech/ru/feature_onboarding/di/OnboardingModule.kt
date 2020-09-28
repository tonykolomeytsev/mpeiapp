package kekmech.ru.feature_onboarding.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher
import kekmech.ru.feature_onboarding.BarsEntryScreenAnalytics
import kekmech.ru.feature_onboarding.OnboardingFeatureLauncherImpl
import kekmech.ru.feature_onboarding.WelcomeScreenAnalytics
import org.koin.dsl.bind

object OnboardingModule : ModuleProvider({
    factory { OnboardingDependencies(get()) } bind OnboardingDependencies::class
    factory { OnboardingFeatureLauncherImpl(get()) } bind OnboardingFeatureLauncher::class

    factory { WelcomeScreenAnalytics(get()) }
    factory { BarsEntryScreenAnalytics(get()) }
})