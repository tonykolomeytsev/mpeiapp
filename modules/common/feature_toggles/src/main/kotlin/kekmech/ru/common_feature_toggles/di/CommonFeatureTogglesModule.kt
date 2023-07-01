package kekmech.ru.common_feature_toggles.di

import kekmech.ru.common_feature_toggles.FeatureToggles
import kekmech.ru.common_feature_toggles.FeatureTogglesImpl
import kekmech.ru.common_feature_toggles.RemoteVariableValueHolder
import kekmech.ru.common_feature_toggles.RemoteVariableValueHolderImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CommonFeatureTogglesModule = module {
    singleOf(::FeatureTogglesImpl) bind FeatureToggles::class
    singleOf(::RemoteVariableValueHolderImpl) bind RemoteVariableValueHolder::class
}
