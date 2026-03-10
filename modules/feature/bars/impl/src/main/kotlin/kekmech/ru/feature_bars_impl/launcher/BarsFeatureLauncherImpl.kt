package kekmech.ru.feature_bars_impl.launcher

import kekmech.ru.feature_bars_api.BarsFeatureLauncher
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.BarsComposeFragment

internal class BarsFeatureLauncherImpl : BarsFeatureLauncher {

    override fun launchMain() = BarsComposeFragment()
}
