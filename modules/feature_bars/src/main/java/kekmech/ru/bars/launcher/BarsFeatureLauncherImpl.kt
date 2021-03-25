package kekmech.ru.bars.launcher

import kekmech.ru.bars.screen.main.BarsFragment
import kekmech.ru.domain_bars.BarsFeatureLauncher

internal class BarsFeatureLauncherImpl : BarsFeatureLauncher {

    override fun launchMain() = BarsFragment()
}