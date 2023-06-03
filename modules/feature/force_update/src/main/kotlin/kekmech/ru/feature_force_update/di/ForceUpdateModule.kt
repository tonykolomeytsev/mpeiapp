package kekmech.ru.feature_force_update.di

import kekmech.ru.domain_force_update.ForceUpdateChecker
import kekmech.ru.feature_force_update.ForceUpdateCheckerImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureForceUpdateModule = module {
    factoryOf(::ForceUpdateCheckerImpl) bind ForceUpdateChecker::class
}
