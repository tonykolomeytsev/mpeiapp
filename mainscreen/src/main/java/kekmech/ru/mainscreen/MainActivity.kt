package kekmech.ru.mainscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import dagger.android.support.DaggerAppCompatActivity
import kekmech.ru.core.Screens
import kekmech.ru.feed.FeedFragment
import kekmech.ru.settings.SettingsDevFragment
import kekmech.ru.settings.SettingsFragment
import kekmech.ru.timetable.TimetableFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.SupportAppNavigator
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    val feedFragment by lazy { FeedFragment() }
    val timetableFragment by lazy { TimetableFragment() }
    val settingsFragment by lazy { SettingsFragment() }
    val settingsDevFragment by lazy { SettingsDevFragment() }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                nav_view.postOnAnimation {
                    router.replaceScreen(Screens.FEED)
                }
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

    val navigator = object : SupportAppNavigator(this, R.id.main_container) {


        override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = null
//            when (screenKey) { "MAIN" -> Intent(this@MainActivity, AuthActivity::class.java)
//            else -> null }

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.FEED -> feedFragment //FeedFragment()
            Screens.TIMETABLE -> timetableFragment
            Screens.SETTINGS -> settingsFragment
            Screens.DEV -> settingsDevFragment
            else -> null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        router.replaceScreen(Screens.FEED)
    }

    @Inject
    lateinit var navigationHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }
}
