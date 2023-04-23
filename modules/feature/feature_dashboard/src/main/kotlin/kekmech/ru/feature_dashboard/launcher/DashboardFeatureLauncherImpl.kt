package kekmech.ru.feature_dashboard.launcher

import androidx.fragment.app.Fragment
import kekmech.ru.domain_dashboard.DashboardFeatureLauncher
import kekmech.ru.feature_dashboard.DashboardFragment

internal class DashboardFeatureLauncherImpl : DashboardFeatureLauncher {

    override fun getScreen(): Fragment = DashboardFragment()
}
