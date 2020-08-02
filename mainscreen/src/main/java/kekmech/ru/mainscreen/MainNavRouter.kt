package kekmech.ru.mainscreen

import androidx.navigation.NavController
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.Screens.*

class MainNavRouter : Router {
    private var navController: NavController? = null

    override fun navigate(fragmentId: Screens) {
        try {
            when (fragmentId) {
                // FEED SCOPE
                FEED_TO_ADD -> navController?.navigate(R.id.action_feedFragment_to_addFragment)
                FEED_TO_FORCE -> try {
                    navController?.navigate(R.id.action_feedFragment_to_forceUpdateFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                FEED_TO_NOTE -> navController?.navigate(R.id.action_feedFragment_to_noteFragment)
                FEED_TO_SETTINGS -> navController?.navigate(R.id.action_feedFragment_to_settingsFragment)

                // settings
                SETTINGS_TO_ADD -> navController?.navigate(R.id.action_settingsFragment_to_addFragment)

                // BARS SCOPE
                BARS_TO_RIGHTS -> navController?.navigate(R.id.action_barsFragment_to_rightsFragment)
                BARS_TO_BARS_DETAILS -> navController?.navigate(R.id.action_barsFragment_to_barsDetailsFragment)
                BARS_TO_FORCE -> navController?.navigate(R.id.action_barsFragment_to_forceUpdateFragment2)
                BARS_TO_RATING -> navController?.navigate(R.id.action_barsFragment_to_ratingFragment)

                // TIMETABLE SCOPE
                TIMETABLE_TO_FORCE -> navController?.navigate(R.id.action_timetableFragment_to_forceUpdateFragment3)
                TIMETABLE_TO_NOTE -> navController?.navigate(R.id.action_timetableFragment_to_noteFragment2)
            }
        }catch (e: Exception) { e.printStackTrace() }
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