package kekmech.ru.feature_main_screen_impl.di

import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenApi
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenDependencyComponent
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTabSwitcher
import kekmech.ru.feature_main_screen_api.presentation.navigation.children.ChildrenResolutionStrategy
import kekmech.ru.feature_main_screen_impl.presentation.navigation.MainScreenApiImpl
import kekmech.ru.feature_main_screen_impl.presentation.navigation.MainScreenDependencyComponentImpl
import kekmech.ru.feature_main_screen_impl.presentation.navigation.MainScreenTabStateHolder
import kekmech.ru.feature_main_screen_impl.presentation.navigation.children.ChildrenResolutionStrategyImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureMainScreenModule = module {
    factoryOf(::MainScreenApiImpl) bind MainScreenApi::class
    singleOf(::MainScreenTabStateHolder) bind MainScreenTabSwitcher::class
    factoryOf(::ChildrenResolutionStrategyImpl) bind ChildrenResolutionStrategy::class
    factoryOf(::MainScreenDependencyComponentImpl) bind MainScreenDependencyComponent::class
}
