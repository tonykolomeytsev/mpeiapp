package kekmech.ru.mainscreen

import androidx.navigation.NavController
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.Screens.*
import javax.inject.Inject

class MainNavRouter @Inject constructor(): Router {
    private var navController: NavController? = null

    override fun navigate(fragmentId: Screens) {
        when(fragmentId) {
            // FEED SCOPE
            FEED_TO_ADD -> navController?.navigate(R.id.action_feedFragment_to_addFragment)
            FEED_TO_FORCE -> try {
                navController?.navigate(R.id.action_feedFragment_to_forceUpdateFragment)
            } catch (e: Exception) { e.printStackTrace() }
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