package kekmech.ru.feature_onboarding.di

import kekmech.ru.common_di.ModuleProvider
import org.koin.dsl.bind

object OnboardingModule : ModuleProvider({
    single { OnboardingDependencies(get()) } bind OnboardingDependencies::class
})