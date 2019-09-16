package kekmech.ru.mainscreen

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private val navController by lazy { MainNavController(this) }
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
    }

    override fun onResume() {
        super.onResume()
        navController.onResume(this)
        (router as MainNavRouter).registerNavController(navController)
        navController.onAddGroupListener = this::toggleBottomNavBar
        if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) == null)
            router.navigateTo(Screens.FEED)
    }

    override fun onPause() {
        super.onPause()
        (router as MainNavRouter).removeNavController()
    }

    private fun toggleBottomNavBar(boolean: Boolean) {
        if (!boolean) nav_view.visibility = View.VISIBLE
        nav_view.animate()
            .alpha(if (boolean) 0f else 1f)
            .setDuration(300)
            .withEndAction { if (boolean) nav_view.visibility = View.GONE }
            .start()

    }

    override fun onBackPressed() {
        router.popBackStack()
    }
}
