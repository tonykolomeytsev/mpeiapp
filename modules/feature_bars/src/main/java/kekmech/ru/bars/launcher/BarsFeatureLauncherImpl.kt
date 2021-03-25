package kekmech.ru.bars.launcher

import kekmech.ru.bars.screen.stub.BarsStubFragment
import kekmech.ru.domain_bars.BarsFeatureLauncher

internal class BarsFeatureLauncherImpl : BarsFeatureLauncher {

    override fun launchMain() = BarsStubFragment()
}