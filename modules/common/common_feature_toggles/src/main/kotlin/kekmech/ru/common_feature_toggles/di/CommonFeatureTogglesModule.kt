package kekmech.ru.common_feature_toggles.di

import kekmech.ru.common_feature_toggles.FeatureToggles
import kekmech.ru.common_feature_toggles.FeatureTogglesImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val CommonFeatureTogglesModule: Module = module {
    singleOf(::FeatureTogglesImpl) bind FeatureToggles::class
}
