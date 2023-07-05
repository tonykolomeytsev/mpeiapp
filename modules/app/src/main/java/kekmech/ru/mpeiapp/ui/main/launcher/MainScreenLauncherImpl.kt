package kekmech.ru.mpeiapp.ui.main.launcher

import kekmech.ru.feature_main_screen_api.MainScreenLauncher
import kekmech.ru.library_navigation.NewRoot
import kekmech.ru.library_navigation.Router
import kekmech.ru.mpeiapp.ui.main.MainFragment

class MainScreenLauncherImpl(
    private val router: Router
) : MainScreenLauncher {

    override fun launch() {
        router.executeCommand(NewRoot { MainFragment.newInstance() })
    }
}
