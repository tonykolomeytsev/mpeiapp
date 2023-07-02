package kekmech.ru.mpeiapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import io.reactivex.rxjava3.disposables.Disposable
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.onActivityResult
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.library_elm.BaseFragment
import kekmech.ru.mpeiapp.R
import kekmech.ru.mpeiapp.databinding.FragmentMainBinding
import kekmech.ru.mpeiapp.ui.main.di.MainScreenDependencies
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenEffect
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenEvent
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenState
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenStoreFactory
import org.koin.android.ext.android.inject

@Suppress("TooManyFunctions")
class MainFragment :
    BaseFragment<MainScreenEvent, MainScreenEffect, MainScreenState>(R.layout.fragment_main),
    BackButtonListener {

    private val dependencies by inject<MainScreenDependencies>()
    private var bottomBarController: BottomBarController? = null
    private var tabsSwitcherDisposable: Disposable? = null
    private val tabsSwitcher by fastLazy { dependencies.bottomTabsSwitcher }
    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    override fun createStore() = MainScreenStoreFactory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            // start observing data
            dependencies.forceUpdateChecker.check()
        } else {
            (savedInstanceState.getSerializable(LAST_TAB_KEY) as? BottomTab)?.let { tab ->
                bottomBarController?.lastSelectedTab = tab
                tabsSwitcher.changeTab(tab)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.bottomNavigation.setOnApplyWindowInsetsListener(null)
        viewBinding.bottomNavigation.setPadding(0)
        view.addSystemBottomPadding()

        val controller = bottomBarController ?: BottomBarController(
            this,
            dependencies.dashboardFeatureLauncher,
            dependencies.scheduleFeatureLauncher,
            dependencies.barsFeatureLauncher,
            dependencies.mapFeatureLauncher
        )
        controller.init(this, viewBinding.bottomNavigation)
        bottomBarController = controller

        val preheatAppSettings = dependencies.appSettingsRepository.getAppSettings()
        if (dependencies.isSnowFlakesEnabledFeatureToggle.value
            && preheatAppSettings.isSnowEnabled
        ) {
            enableSnowFlakesEffect(view)
        }
    }

    override fun render(state: MainScreenState) = Unit

    private fun enableSnowFlakesEffect(view: View) {
        (view as ViewGroup).addView(
            View.inflate(requireContext(), R.layout.item_snow_flakes, null)
        )
    }

    override fun onResume() {
        super.onResume()
        tabsSwitcherDisposable = tabsSwitcher.observe().subscribe {
            it.map { tab ->
                bottomBarController?.switchTab(tab)
                tabsSwitcher.clearTab()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        tabsSwitcherDisposable?.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bottomBarController?.lastSelectedTab?.let {
            outState.putSerializable(LAST_TAB_KEY, it)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        childFragmentManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed(): Boolean {
        return bottomBarController?.popStack() == true
    }

    companion object {

        private const val LAST_TAB_KEY = "lastSelectedTab"

        fun newInstance() = MainFragment()
    }
}
