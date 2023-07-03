package kekmech.ru.mpeiapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_bars_api.BarsFeatureLauncher
import kekmech.ru.feature_dashboard_api.DashboardFeatureLauncher
import kekmech.ru.library_navigation.BottomTab
import kekmech.ru.library_navigation.features.ScrollToTop
import kekmech.ru.library_navigation.features.TabScreenStateSaver
import kekmech.ru.mpeiapp.R
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val FRAGMENT_POSTPONE_DELAY = 300L

class BottomBarController(
    fragment: Fragment,
    private val dashboardFeatureLauncher: DashboardFeatureLauncher,
    private val scheduleFeatureLauncher: ScheduleFeatureLauncher,
    private val barsFeatureLauncher: BarsFeatureLauncher,
    private val mapFeatureLauncher: MapFeatureLauncher,
) {

    private val childFragmentManager: FragmentManager = fragment.childFragmentManager
    var lastSelectedTab = BottomTab.DASHBOARD
    private var bottomNavView: BottomNavigationView? = null
    private val bundle: Bundle = Bundle()
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

    private fun selectTab(tab: BottomTab) {
        if (currentTabFragment?.tag == tab.name) {
            (currentTabFragment as? ScrollToTop)?.onScrollToTop()
            return
        }

        (currentTabFragment as? TabScreenStateSaver)?.updateBundle(bundle)

        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, createTabFragment(tab), tab.name)
        }.commitNowIgnoreStateLossError()
        lastSelectedTab = tab
    }

    private fun FragmentTransaction.commitNowIgnoreStateLossError() = try {
        commitNowAllowingStateLoss()
    } catch (e: Exception) {
        Timber.e(e)
        // TODO: remove in future if will no exceptions
        FirebaseCrashlytics.getInstance().recordException(e)
    }

    private fun getItemByTab(tab: BottomTab) = when (tab) {
        BottomTab.DASHBOARD -> R.id.navigation_dashboard
        BottomTab.SCHEDULE -> R.id.navigation_schedule
        BottomTab.MAP -> R.id.navigation_map
        BottomTab.PROFILE -> R.id.navigation_profile
    }

    private fun createTabFragment(tab: BottomTab): Fragment = when (tab) {
        BottomTab.DASHBOARD -> dashboardFeatureLauncher.getScreen()
        BottomTab.SCHEDULE -> scheduleFeatureLauncher.getScreen()
        BottomTab.MAP -> mapFeatureLauncher.launchMain()
        BottomTab.PROFILE -> barsFeatureLauncher.launchMain()
    }.apply { (this as? TabScreenStateSaver)?.restoreBundle(bundle) }
}
