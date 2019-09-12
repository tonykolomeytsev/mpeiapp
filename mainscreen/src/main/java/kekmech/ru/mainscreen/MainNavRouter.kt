package kekmech.ru.mainscreen

import androidx.navigation.NavController
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import javax.inject.Inject

class MainNavRouter @Inject constructor(): Router {
    private var navController: NavController? = null

    override fun navigateTo(fragmentId: String) {
        when(fragmentId) {
            Screens.FEED -> navController?.navigate(R.id.feedFragment)
            Screens.TIMETABLE -> navController?.navigate(R.id.timetableFragment)
            Screens.SETTINGS -> navController?.navigate(R.id.settingsFragment)
            Screens.DEV -> navController?.navigate(R.id.action_settingsFragment_to_settingsDevFragment)
        }
    }

    override fun replaceScreen(fragmentId: String) {
        when(fragmentId) {
            Screens.FEED -> navController?.navigate(R.id.feedFragment)
            Screens.TIMETABLE -> navController?.navigate(R.id.timetableFragment)
            Screens.SETTINGS -> navController?.navigate(R.id.settingsFragment)
            Screens.DEV -> navController?.navigate(R.id.action_settingsFragment_to_settingsDevFragment)
        }
    }

    override fun popBackStack() {
        navController?.popBackStack()
    }

    fun registerNavController(navController: NavController) {
        this.navController = navController
    }

    fun removeNavController() {
        this.navController = null
    }

}