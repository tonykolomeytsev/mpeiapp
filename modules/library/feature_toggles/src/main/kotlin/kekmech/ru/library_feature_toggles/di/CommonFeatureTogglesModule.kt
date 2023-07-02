package kekmech.ru.library_feature_toggles.di

import kekmech.ru.library_feature_toggles.RemoteVariableValueHolder
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val CommonFeatureTogglesModule = module {
    factoryOf(::RemoteVariableValueHolder)
}
