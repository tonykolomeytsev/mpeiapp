package kekmech.ru.lib_feature_toggles.di

import kekmech.ru.lib_feature_toggles.RemoteVariableValueHolder
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val LibraryFeatureTogglesModule = module {
    factoryOf(::RemoteVariableValueHolder)
}
