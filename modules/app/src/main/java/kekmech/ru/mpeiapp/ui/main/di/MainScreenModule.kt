package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.feature_main_screen_api.MainScreenLauncher
import kekmech.ru.library_navigation.BottomTabsSwitcher
import kekmech.ru.mpeiapp.ui.main.BottomTabsSwitcherImpl
import kekmech.ru.mpeiapp.ui.main.launcher.MainScreenLauncherImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val MainScreenModule = module {
    single { BottomTabsSwitcherImpl } bind BottomTabsSwitcher::class
    singleOf(::MainScreenDependencies)
    factoryOf(::MainScreenLauncherImpl) bind MainScreenLauncher::class
}
