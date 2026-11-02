package kekmech.ru.feature_onboarding_impl.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_onboarding_api.OnboardingFeatureApi
import kekmech.ru.feature_onboarding_impl.presentation.navigation.OnboardingFeatureApiImpl
import kekmech.ru.feature_onboarding_impl.presentation.navigation.OnboardingNavEntries
import kekmech.ru.lib_navigation.NavEntries
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureOnboardingModule = module {
    factoryOf(::OnboardingDependencies)
    factoryOf(::OnboardingFeatureApiImpl) bind OnboardingFeatureApi::class
    factoryOf(::OnboardingNavEntries) bindIntoList NavEntries::class
}
