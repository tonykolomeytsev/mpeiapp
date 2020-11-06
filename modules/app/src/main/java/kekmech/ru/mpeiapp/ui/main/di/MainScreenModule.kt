package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.domain_main_screen.MainScreenLauncher
import kekmech.ru.mpeiapp.ui.main.BottomTabsSwitcherImpl
import kekmech.ru.mpeiapp.ui.main.launcher.MainScreenLauncherImpl
import org.koin.dsl.bind

object MainScreenModule : ModuleProvider({
    single { BottomTabsSwitcherImpl } bind BottomTabsSwitcher::class
    single { MainScreenDependencies(get(), get(), get(), get(), get(), get(), get()) } bind MainScreenDependencies::class
    factory { MainScreenLauncherImpl(get()) } bind MainScreenLauncher::class
})