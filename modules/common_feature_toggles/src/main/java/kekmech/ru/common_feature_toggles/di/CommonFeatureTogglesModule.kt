package kekmech.ru.common_feature_toggles.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_feature_toggles.FeatureToggles
import kekmech.ru.common_feature_toggles.FeatureTogglesImpl
import kekmech.ru.common_feature_toggles.RemoteConfigWrapper
import org.koin.dsl.bind

object CommonFeatureTogglesModule : ModuleProvider({
    single { FeatureTogglesImpl(get()) } bind FeatureToggles::class
    factory { RemoteConfigWrapper() }
})