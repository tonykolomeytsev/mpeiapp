package kekmech.ru.feature_dashboard

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.openLinkExternal
import kekmech.ru.common_android.views.setProgressViewOffset
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.AddActionAdapterItem
import kekmech.ru.coreui.items.NoteAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.feature_dashboard.di.DashboardDependencies
import kekmech.ru.feature_dashboard.items.*
import kekmech.ru.feature_dashboard.presentation.DashboardEffect
import kekmech.ru.feature_dashboard.presentation.DashboardEvent
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.Wish
import kekmech.ru.feature_dashboard.presentation.DashboardFeature
import kekmech.ru.feature_dashboard.presentation.DashboardState
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.ext.android.inject

class DashboardFragment : BaseFragment<DashboardEvent, DashboardEffect, DashboardState, DashboardFeature>() {

    override val initEvent = Wish.Init

    private val dependencies by inject<DashboardDependencies>()

    override fun createFeature() = dependencies.dashboardFeatureFactory.create()

    override var layoutId = R.layout.fragment_dashboard

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addSystemVerticalPadding()
        bannerContainer.addSystemTopPadding()
        swipeRefresh.apply {
            setOnRefreshListener { feature.accept(Wish.Action.OnSwipeRefresh) }
            doOnApplyWindowInsets { _, windowInsets, _ ->
                setProgressViewOffset(windowInsets.systemWindowInsetTop)
            }
        }
    }

    override fun render(state: DashboardState) {
        adapter.update(DashboardListConverter(requireContext()).map(state))
        swipeRefresh.post {
            swipeRefresh.isRefreshing = state.isSwipeRefreshLoadingAnimation
        }
    }

    override fun handleEffect(effect: DashboardEffect) = when (effect) {
        is DashboardEffect.ShowLoadingError -> showBanner(R.string.dashboard_loading_error)
    }

    override fun onResume() {
        super.onResume()
        feature.accept(Wish.Action.OnSwipeRefresh)
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        SearchFieldAdapterItem { /* on click search field */ },
        BannerLunchAdapterItem {
            dependencies.bottomTabsSwitcher.changeTab(BottomTab.MAP)
        },
        BannerOpenSourceAdapterItem {
            requireContext().openLinkExternal("https://vk.com/kekmech")
        },
        SectionHeaderAdapterItem(),
        NoteAdapterItem(requireContext()),
        AddActionAdapterItem(),
        DayStatusAdapterItem(),
        DashboardClassesAdapterItem(requireContext()),
        DashboardClassesMinAdapterItem(requireContext()),
        ChangeGroupAdapterItem {
            dependencies.scheduleFeatureLauncher.launchSearchGroup()
        }
    )
}