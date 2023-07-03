package kekmech.ru.feature_bars_impl.launcher

import kekmech.ru.feature_bars_api.BarsFeatureLauncher
import kekmech.ru.feature_bars_impl.presentation.screen.main.BarsFragment

internal class BarsFeatureLauncherImpl : BarsFeatureLauncher {

    override fun launchMain() = BarsFragment()
}
