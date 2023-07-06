package kekmech.ru.feature_dashboard_impl.launcher

import androidx.fragment.app.Fragment
import kekmech.ru.feature_dashboard_api.DashboardFeatureLauncher
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardFragment

internal class DashboardFeatureLauncherImpl : DashboardFeatureLauncher {

    override fun getScreen(): Fragment = DashboardFragment()
}
