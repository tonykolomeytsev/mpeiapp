package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.mpeiapp.Prefetcher
import kekmech.ru.mpeiapp.ui.main.BottomTabsSwitcher

data class MainScreenDependencies(
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val prefetcher: Prefetcher
)