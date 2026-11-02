package kekmech.ru.feature_app_update_impl.di

import kekmech.ru.feature_app_update_api.ForceUpdateChecker
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_app_update_impl.ForceUpdateCheckerImpl
import kekmech.ru.feature_app_update_impl.navigation.AppUpdateNavEntries
import kekmech.ru.lib_navigation.NavEntries
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureForceUpdateModule = module {
    factoryOf(::ForceUpdateCheckerImpl) bind ForceUpdateChecker::class
    factoryOf(::AppUpdateNavEntries) bindIntoList NavEntries::class
}
