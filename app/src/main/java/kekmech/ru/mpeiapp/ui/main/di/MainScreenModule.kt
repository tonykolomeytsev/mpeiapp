package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.common_navigation.di.MainFragmentHolder
import kekmech.ru.mpeiapp.ui.main.BottomTabsSwitcherImpl
import kekmech.ru.mpeiapp.ui.main.MainFragment
import org.koin.dsl.bind

object MainScreenModule : ModuleProvider({
    single { BottomTabsSwitcherImpl } bind BottomTabsSwitcher::class
    single { MainScreenDependencies(get(), get(), get(), get()) } bind MainScreenDependencies::class
    factory { MainFragment.newInstance() } bind MainFragmentHolder::class
})