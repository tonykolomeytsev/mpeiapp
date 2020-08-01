package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.mpeiapp.ui.main.BottomTabsSwitcher
import kekmech.ru.mpeiapp.ui.main.BottomTabsSwitcherImpl
import org.koin.dsl.bind

object MainScreenModule : ModuleProvider({
    single { BottomTabsSwitcherImpl } bind BottomTabsSwitcher::class
})