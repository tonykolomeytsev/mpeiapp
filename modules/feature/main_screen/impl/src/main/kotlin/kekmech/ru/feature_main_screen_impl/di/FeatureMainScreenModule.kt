package kekmech.ru.feature_main_screen_impl.di

import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenNavigationApi
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTabSwitcher
import kekmech.ru.feature_main_screen_impl.presentation.navigation.MainScreenNavigationApiImpl
import kekmech.ru.feature_main_screen_impl.presentation.navigation.MainScreenTabStateHolder
import kekmech.ru.feature_main_screen_impl.presentation.navigation.children.ChildrenResolutionStrategy
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureMainScreenModule = module {
    factoryOf(::MainScreenNavigationApiImpl) bind MainScreenNavigationApi::class
    singleOf(::MainScreenTabStateHolder) bind MainScreenTabSwitcher::class
    factoryOf(::ChildrenResolutionStrategy)
}
