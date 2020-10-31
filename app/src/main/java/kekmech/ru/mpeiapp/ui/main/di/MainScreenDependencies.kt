package kekmech.ru.mpeiapp.ui.main.di

import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.mpeiapp.Prefetcher

data class MainScreenDependencies(
    val bottomTabsSwitcher: BottomTabsSwitcher,
    val prefetcher: Prefetcher,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    val barsFeatureLauncher: BarsFeatureLauncher,
    val mapFeatureLauncher: MapFeatureLauncher
)