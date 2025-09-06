package kekmech.ru.mpeiapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kekmech.ru.ext_android.addSystemBottomPadding
import kekmech.ru.ext_android.onActivityResult
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.lib_navigation.BackButtonListener
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.mpeiapp.R
import kekmech.ru.mpeiapp.databinding.FragmentMainBinding
import kekmech.ru.mpeiapp.ui.main.di.MainScreenDependencies
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenEffect
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenState
import kekmech.ru.mpeiapp.ui.main.elm.MainScreenStoreFactory
import kotlinx.coroutines.launch
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject

@Suppress("TooManyFunctions")
class MainFragment :
    Fragment(R.layout.fragment_main),
    ElmRendererDelegate<MainScreenEffect, MainScreenState>,
    BackButtonListener {

    private val dependencies by inject<MainScreenDependencies>()
    private var bottomBarController: BottomBarController? = null
    private val tabsSwitcher by fastLazy { dependencies.bottomTabsSwitcher }
    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    @Suppress("Unused")
    private val store by androidElmStore { MainScreenStoreFactory.create() }

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
            dependencies.scheduleFeatureApi,
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
        lifecycleScope.launch {
            tabsSwitcher.observe().collect { tab ->
                tab?.let {
                    bottomBarController?.switchTab(it)
                    tabsSwitcher.clearTab()
                }
            }
        }
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
