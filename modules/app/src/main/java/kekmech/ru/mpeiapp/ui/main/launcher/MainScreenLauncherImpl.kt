package kekmech.ru.mpeiapp.ui.main.launcher

import kekmech.ru.domain_main_screen.MainScreenLauncher
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
