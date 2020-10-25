package kekmech.ru.mpeiapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_dashboard.DashboardFragment
import kekmech.ru.map.MapFragment
import kekmech.ru.mpeiapp.R
import java.util.concurrent.TimeUnit

class BottomBarController(
    fragment: Fragment,
    private val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    private val barsFeatureLauncher: BarsFeatureLauncher
) {

    private val childFragmentManager: FragmentManager = fragment.childFragmentManager
    var lastSelectedTab = BottomTab.DASHBOARD
    private var bottomNavView: BottomNavigationView? = null
    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden }
    private val backStack: BottomBarBackStack = BottomBarBackStack(firstTab = BottomTab.DASHBOARD)

    private val navSelectListener = BottomNavigationView.OnNavigationItemSelectedListener {  item ->
        val tab = when (item.itemId) {
            R.id.navigation_dashboard -> BottomTab.DASHBOARD
            R.id.navigation_schedule -> BottomTab.SCHEDULE
            R.id.navigation_map -> BottomTab.MAP
            R.id.navigation_profile -> BottomTab.PROFILE
            else -> null
        }
        tab?.let {
            selectTab(it)
            backStack.push(it)
        }
        true
    }

    fun init(
        containerFragment: Fragment,
        bottomNavView: BottomNavigationView
    ) {
        this.bottomNavView = bottomNavView
        bottomNavView.setOnNavigationItemSelectedListener(navSelectListener)
        selectTab(lastSelectedTab)
        backStack.push(lastSelectedTab)
        if (bottomNavView.selectedItemId == R.id.navigation_dashboard) {
            containerFragment.postponeEnterTransition(300L, TimeUnit.MILLISECONDS)
        }
    }

    fun switchTab(tab: BottomTab) {
        bottomNavView?.let { it.selectedItemId = getItemByTab(tab) }
    }

    fun popStack(): Boolean = backStack.popAndPeek()?.let { switchTab(it) } != null

    @Suppress("DEPRECATION")
    private fun selectTab(tab: BottomTab) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.name)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) {
                add(R.id.fragmentContainer, createTabFragment(tab), tab.name)
            }
            currentFragment?.let { fragment ->
                hide(fragment)
                fragment.userVisibleHint = false
            }
            newFragment?.let { fragment ->
                show(fragment)
                fragment.userVisibleHint = true
            }
        }.commitNow()
        lastSelectedTab = tab
    }

    private fun getItemByTab(tab: BottomTab) = when (tab) {
        BottomTab.DASHBOARD -> R.id.navigation_dashboard
        BottomTab.SCHEDULE -> R.id.navigation_schedule
        BottomTab.MAP -> R.id.navigation_map
        BottomTab.PROFILE -> R.id.navigation_profile
    }

    private fun createTabFragment(tab: BottomTab): Fragment = when (tab) {
        BottomTab.DASHBOARD -> DashboardFragment()
        BottomTab.SCHEDULE -> scheduleFeatureLauncher.launchMain()
        BottomTab.MAP -> MapFragment()
        BottomTab.PROFILE -> barsFeatureLauncher.launchMain()
    }
}