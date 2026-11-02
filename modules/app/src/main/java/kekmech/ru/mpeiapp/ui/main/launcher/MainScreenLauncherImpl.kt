package kekmech.ru.mpeiapp.ui.main.launcher

import kekmech.ru.feature_app_update_api.ForceUpdateChecker
import kekmech.ru.feature_main_screen_api.MainScreenLauncher
import kekmech.ru.lib_navigation.DashboardTabKey
import kekmech.ru.lib_navigation.NewRoot
import kekmech.ru.lib_navigation.Router

class MainScreenLauncherImpl(
    private val router: Router,
    private val forceUpdateChecker: ForceUpdateChecker,
) : MainScreenLauncher {

    override fun launch() {
        router.executeCommand(NewRoot(DashboardTabKey))
        forceUpdateChecker.check()
    }
}
