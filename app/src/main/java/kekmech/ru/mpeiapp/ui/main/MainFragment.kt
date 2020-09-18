package kekmech.ru.mpeiapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kekmech.ru.common_android.onActivityResult
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.mpeiapp.R
import kekmech.ru.mpeiapp.ui.main.di.MainScreenDependencies
import kekmech.ru.mpeiapp.ui.main.presentation.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment<MainScreenEvent, MainScreenEffect, MainScreenState, MainScreenFeature>(), BackButtonListener {

    override val initEvent: MainScreenEvent get() = MainScreenEvent.Wish.Init
    override var layoutId: Int = R.layout.fragment_main

    override fun createFeature() = MainScreenFeatureFactory.create()

    private val dependencies by inject<MainScreenDependencies>()
    private var bottomBarController: BottomBarController? = null
    private var tabsSwitcherDisposable: Disposable? = null
    private val tabsSwitcher by fastLazy { dependencies.bottomTabsSwitcher }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            // start observing data
        }

        dependencies.prefetcher.prefetch()
    }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)

        val controller = bottomBarController ?: BottomBarController(this)
        controller.init(this, bottomNavigation)
        bottomBarController = controller
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        childFragmentManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed(): Boolean {
        return bottomBarController?.popStack() != true
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}