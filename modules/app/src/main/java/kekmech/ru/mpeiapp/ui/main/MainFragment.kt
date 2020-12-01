package kekmech.ru.mpeiapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.onActivityResult
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.mpeiapp.R
import kekmech.ru.mpeiapp.databinding.FragmentMainBinding
import kekmech.ru.mpeiapp.ui.main.di.MainScreenDependencies
import kekmech.ru.mpeiapp.ui.main.presentation.*
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment<MainScreenEvent, MainScreenEffect, MainScreenState, MainScreenFeature>(), BackButtonListener {

    override val initEvent: MainScreenEvent get() = MainScreenEvent.Wish.Init
    override var layoutId: Int = R.layout.fragment_main

    override fun createFeature() = MainScreenFeatureFactory.create()

    private val dependencies by inject<MainScreenDependencies>()
    private var bottomBarController: BottomBarController? = null
    private var tabsSwitcherDisposable: Disposable? = null
    private val tabsSwitcher by fastLazy { dependencies.bottomTabsSwitcher }
    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            // start observing data
            dependencies.forceUpdateChecker.check()
        }

        dependencies.prefetcher.prefetch()
    }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)
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
        savedInstanceState?.get("lastSelectedTab")?.let {
            bottomBarController?.switchTab(it as BottomTab)
        }

        if (dependencies.featureToggles.isSnowFlakesEnabled && dependencies.appSettings.isSnowEnabled) {
            enableSnowFlakesEffect(view)
        }
    }

    private fun enableSnowFlakesEffect(view: View) {
        (view as ViewGroup).addView(
            View.inflate(requireContext(), R.layout.item_snow_flakes, null)
        )
    }

    override fun onResume() {
        super.onResume()
        tabsSwitcherDisposable = tabsSwitcher.observe().subscribeBy {
            it.value?.let { tab ->
                bottomBarController?.switchTab(tab)
                tabsSwitcher.clearTab()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        tabsSwitcherDisposable?.dispose()
        parentFragmentManager.saveFragmentInstanceState(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bottomBarController?.lastSelectedTab?.let {
            outState.putSerializable("lastSelectedTab", it)
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
        fun newInstance() = MainFragment()
    }
}