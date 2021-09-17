package kekmech.ru.mpeiapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.features.NeedToUpdate
import kekmech.ru.common_navigation.features.ScrollToTop
import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_dashboard.DashboardFragment
import kekmech.ru.mpeiapp.R
import java.util.concurrent.TimeUnit

private const val FRAGMENT_POSTPONE_DELAY = 300L

class BottomBarController(
    fragment: Fragment,
    private val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    private val barsFeatureLauncher: BarsFeatureLauncher,
    private val mapFeatureLauncher: MapFeatureLauncher,
) {

    private val childFragmentManager: FragmentManager = fragment.childFragmentManager
    var lastSelectedTab = BottomTab.DASHBOARD
    private var bottomNavView: BottomNavigationView? = null
    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden }
    private val backStack: BottomBarBackStack = BottomBarBackStack(firstTab = BottomTab.DASHBOARD)

    private val navSelectListener = NavigationBarView.OnItemSelectedListener { item ->
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
        bottomNavView: BottomNavigationView,
    ) {
        this.bottomNavView = bottomNavView
        bottomNavView.setOnItemSelectedListener(navSelectListener)
        selectTab(lastSelectedTab)
        backStack.push(lastSelectedTab)
        if (bottomNavView.selectedItemId == R.id.navigation_dashboard) {
            containerFragment
                .postponeEnterTransition(FRAGMENT_POSTPONE_DELAY, TimeUnit.MILLISECONDS)
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

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) {
            if (currentFragment is NeedToUpdate) currentFragment.onUpdate()
            if (currentFragment is ScrollToTop) currentFragment.onScrollToTop()
            return
        }

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
                if (fragment is NeedToUpdate) {
                    fragment.onUpdate()
                }
                fragment.userVisibleHint = true
            }
        }.commitNowIgnoreStateLossError()
        lastSelectedTab = tab
    }

    private fun FragmentTransaction.commitNowIgnoreStateLossError() = try {
        commitNow()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun getItemByTab(tab: BottomTab) = when (tab) {
        BottomTab.DASHBOARD -> R.id.navigation_dashboard
        BottomTab.SCHEDULE -> R.id.navigation_schedule
        BottomTab.MAP -> R.id.navigation_map
        BottomTab.PROFILE -> R.id.navigation_profile
    }

    private fun createTabFragment(tab: BottomTab): Fragment = when (tab) {
        BottomTab.DASHBOARD -> DashboardFragment()
        BottomTab.SCHEDULE -> scheduleFeatureLauncher.getScreen()
        BottomTab.MAP -> mapFeatureLauncher.launchMain()
        BottomTab.PROFILE -> barsFeatureLauncher.launchMain()
    }
}