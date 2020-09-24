package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.mpeiapp.Prefetcher

data class MainScreenDependencies(
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val prefetcher: Prefetcher
)