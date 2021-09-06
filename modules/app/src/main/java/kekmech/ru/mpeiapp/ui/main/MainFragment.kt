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
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.mpeiapp.R
import kekmech.ru.mpeiapp.databinding.FragmentMainBinding
import kekmech.ru.mpeiapp.ui.main.di.MainScreenDependencies
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenEffect
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenEvent
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenFeatureFactory
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenState
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment<MainScreenEvent, MainScreenEffect, MainScreenState>(), BackButtonListener {

    override val initEvent: MainScreenEvent get() = MainScreenEvent.Wish.Init
    override var layoutId: Int = R.layout.fragment_main

    private val dependencies by inject<MainScreenDependencies>()
    private var bottomBarController: BottomBarController? = null
    private var tabsSwitcherDisposable: Disposable? = null
    private val tabsSwitcher by fastLazy { dependencies.bottomTabsSwitcher }
    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    override fun createStore() = MainScreenFeatureFactory.create()

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

        dependencies.prefetcher.prefetch()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.bottomNavigation.setOnApplyWindowInsetsListener(null)
        viewBinding.bottomNavigation.setPadding(0)
        view.addSystemBottomPadding()

        val controller = bottomBarController ?: BottomBarController(
            this,
            dependencies.scheduleFeatureLauncher,
            dependencies.barsFeatureLauncher,
            dependencies.mapFeatureLauncher
        )
        controller.init(this, viewBinding.bottomNavigation)
        bottomBarController = controller

        if (dependencies.featureToggles.isSnowFlakesEnabled && dependencies.appSettings.isSnowEnabled) {
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
            it.value?.let { tab ->
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