package kekmech.ru.mainscreen

import androidx.navigation.NavController
import com.example.map.view.MapFragment
import kekmech.ru.addscreen.AddFragment
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.Screens.*
import kekmech.ru.feed.FeedFragment
import kekmech.ru.settings.SettingsDevFragment
import kekmech.ru.settings.SettingsFragment
import kekmech.ru.timetable.view.TimetableFragment
import javax.inject.Inject

class MainNavRouter @Inject constructor(): Router {
    private var navController: NavController? = null

    override fun navigate(fragmentId: Screens) {
        when(fragmentId) {
//            FEED -> navController?.navigate(FeedFragment::class, false)
//            TIMETABLE -> navController?.navigate(TimetableFragment::class, false)
//            SETTINGS -> navController?.navigate(SettingsFragment::class, false)
//            MAP -> navController?.navigate(MapFragment::class, false)
//
//            ADD -> navController?.navigate(AddFragment::class, true)
            FEED_TO_ADD -> navController?.navigate(R.id.action_feedFragment_to_addFragment)
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