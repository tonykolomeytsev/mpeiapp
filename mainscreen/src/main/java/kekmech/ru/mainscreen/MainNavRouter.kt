package kekmech.ru.mainscreen

import com.example.map.view.MapFragment
import kekmech.ru.addscreen.AddFragment
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.feed.FeedFragment
import kekmech.ru.settings.SettingsDevFragment
import kekmech.ru.settings.SettingsFragment
import kekmech.ru.timetable.view.TimetableFragment
import javax.inject.Inject

class MainNavRouter @Inject constructor(): Router {
    private var navController: MainNavController? = null

    override fun navigateTo(fragmentId: String) {
        when(fragmentId) {
            Screens.FEED -> navController?.navigate(FeedFragment::class, false)
            Screens.TIMETABLE -> navController?.navigate(TimetableFragment::class, false)
            Screens.SETTINGS -> navController?.navigate(SettingsFragment::class, false)
            Screens.DEV -> navController?.navigateWithTransition(SettingsDevFragment::class, true)
            Screens.MAP -> navController?.navigate(MapFragment::class, false)

            Screens.ADD -> navController?.navigate(AddFragment::class, true)
            Screens.ADD_TO_FEED -> navController?.navigate(FeedFragment::class, false, action = Screens.ADD_TO_FEED)
        }
    }

    override fun replaceScreen(fragmentId: String) = navigateTo(fragmentId)

    override fun popBackStack() {
        navController?.popBackStack()
    }

    fun registerNavController(navController: MainNavController) {
        this.navController = navController
    }

    fun removeNavController() {
        this.navController = null
    }

}