package kekmech.ru.feature_bars.launcher

import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.feature_bars.screen.main.BarsFragment

internal class BarsFeatureLauncherImpl : BarsFeatureLauncher {

    override fun launchMain() = BarsFragment()
}
