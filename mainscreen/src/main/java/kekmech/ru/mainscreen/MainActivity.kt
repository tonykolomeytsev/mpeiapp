package kekmech.ru.mainscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.LinearInterpolator
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.Navigation
import dagger.android.support.DaggerAppCompatActivity
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.feed.FeedFragment
import kekmech.ru.settings.SettingsDevFragment
import kekmech.ru.settings.SettingsFragment
import kekmech.ru.timetable.TimetableFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private val navController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }
    @Inject lateinit var router: Router

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                nav_view.postOnAnimation { router.replaceScreen(Screens.FEED) }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                router.replaceScreen(Screens.TIMETABLE)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                router.replaceScreen(Screens.SETTINGS)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navController // init
    }

    override fun onResume() {
        super.onResume()
        (router as MainNavRouter).registerNavController(navController)
    }

    override fun onPause() {
        super.onPause()
        (router as MainNavRouter).removeNavController()
    }
}
