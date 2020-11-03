package kekmech.ru.bars.di

import kekmech.ru.bars.launcher.BarsFeatureLauncherImpl
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_bars.BarsFeatureLauncher
import org.koin.dsl.bind

object BarsModule : ModuleProvider({
    factory { BarsFeatureLauncherImpl() } bind BarsFeatureLauncher::class
})