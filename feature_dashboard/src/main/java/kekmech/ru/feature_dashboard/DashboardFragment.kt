package kekmech.ru.feature_dashboard

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.views.setProgressViewOffset
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
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
        is DashboardEffect.ShowLoadingError -> showBanner(R.string.something_went_wrong_error)
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        SearchFieldAdapterItem { /* on click search field */ },
        BannerLunchAdapterItem { /* on click lunch banner */ },
        BannerOpenSourceAdapterItem { /* on click github banner */ },
        SectionHeaderAdapterItem(SECTION_HEADER_EVENTS),
        SectionHeaderAdapterItem(SECTION_HEADER_NOTES),
        SectionHeaderAdapterItem(SECTION_HEADER_REMINDERS),
        NoteAdapterItem(requireContext()),
        AddActionAdapterItem(),
        DayStatusAdapterItem(),
        DashboardClassesAdapterItem(requireContext()),
        DashboardClassesMinAdapterItem(requireContext())
    )

    companion object {
        const val SECTION_HEADER_EVENTS = 0
        const val SECTION_HEADER_NOTES = 1
        const val SECTION_HEADER_REMINDERS = 2
    }
}