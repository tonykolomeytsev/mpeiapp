package kekmech.ru.lib_feature_toggles.di

import kekmech.ru.lib_feature_toggles.RemoteVariableValueHolder
import org.koin.core.module.Module
import org.koin.dsl.module

public val LibraryFeatureTogglesModule: Module = module {
    factory {
        RemoteVariableValueHolder(
            remoteConfigWrapper = get(),
            middleware = getOrNull(),
        )
    }
}
