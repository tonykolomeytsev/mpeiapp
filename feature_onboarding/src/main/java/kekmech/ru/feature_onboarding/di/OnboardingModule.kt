package kekmech.ru.feature_onboarding.di

import kekmech.ru.common_di.ModuleProvider

object OnboardingModule : ModuleProvider({
    single { OnboardingDependencies() }
})