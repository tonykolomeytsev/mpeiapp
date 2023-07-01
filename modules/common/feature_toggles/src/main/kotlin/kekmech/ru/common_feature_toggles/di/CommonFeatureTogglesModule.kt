package kekmech.ru.common_feature_toggles.di

import kekmech.ru.common_feature_toggles.RemoteVariableValueHolder
import kekmech.ru.common_feature_toggles.RemoteVariableValueHolderImpl
import kekmech.ru.common_feature_toggles.RewriteRemoteVariableHandle
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val CommonFeatureTogglesModule = module {
    singleOf(::RemoteVariableValueHolderImpl) binds arrayOf(
        RemoteVariableValueHolder::class,
        RewriteRemoteVariableHandle::class,
    )
}
