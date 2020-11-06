package kekmech.ru.feature_dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.views.setProgressViewOffset
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.NeedToUpdate
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_schedule.CONTINUE_TO_BACK_STACK_WITH_RESULT
import kekmech.ru.feature_dashboard.di.DashboardDependencies
import kekmech.ru.feature_dashboard.items.*
import kekmech.ru.feature_dashboard.presentation.DashboardEffect
import kekmech.ru.feature_dashboard.presentation.DashboardEvent
import kekmech.ru.feature_dashboard.presentation.DashboardEvent.Wish
import kekmech.ru.feature_dashboard.presentation.DashboardFeature
import kekmech.ru.feature_dashboard.presentation.DashboardState
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.ext.android.inject

private const val REQUEST_CODE_UPDATE_DATA = 2910
private const val SHARED_PREFS_BANNERS = "banners"

class DashboardFragment : BaseFragment<DashboardEvent, DashboardEffect, DashboardState, DashboardFeature>(),
    ActivityResultListener,
    NeedToUpdate {

    override val initEvent = Wish.Init

    private val dependencies by inject<DashboardDependencies>()

    override fun createFeature() = dependencies.dashboardFeatureFactory.create()

    override var layoutId = R.layout.fragment_dashboard

    private val adapter by fastLazy { createAdapter() }

    private val analytics: DashboardAnalytics by inject()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addSystemVerticalPadding()
        recyclerView.addScrollAnalytics(analytics, "RecyclerView")
        bannerContainer.addSystemTopPadding()
        swipeRefresh.apply {
            setOnRefreshListener { feature.accept(Wish.Action.OnSwipeRefresh) }
            doOnApplyWindowInsets { _, windowInsets, _ ->
                setProgressViewOffset(windowInsets.systemWindowInsetTop)
            }
        }
        analytics.sendScreenShown()
    }

    override fun render(state: DashboardState) {
        adapter.update(DashboardListConverter(requireContext()).map(state))
        swipeRefresh.post {
            swipeRefresh.isRefreshing = state.isSwipeRefreshLoadingAnimation
        }
    }

    override fun handleEffect(effect: DashboardEffect) = when (effect) {
        is DashboardEffect.ShowLoadingError -> showBanner(R.string.dashboard_loading_error)
        is DashboardEffect.ShowNotesLoadingError -> showBanner(R.string.something_went_wrong_error)
        is DashboardEffect.NavigateToNotesList -> {
            dependencies.notesFeatureLauncher.launchNoteList(
                selectedClasses = effect.classes,
                selectedDate = effect.date,
                targetFragment = parentFragment,
                requestCode = REQUEST_CODE_UPDATE_DATA
            )
        }
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        SearchFieldAdapterItem {
            analytics.sendClick("Search")
            dependencies.searchFeatureLauncher.launch()
        },
        BannerLunchAdapterItem {
            analytics.sendClick("BannerLunch")
            dependencies.bottomTabsSwitcher.changeTab(BottomTab.MAP)
        },
        BannerOpenSourceAdapterItem {
            analytics.sendClick("BannerOpenSource")
            requireContext().openLinkExternal("https://vk.com/kekmech")
        },
        SectionHeaderAdapterItem {
            analytics.sendClick("ShowAllNotes")
            dependencies.notesFeatureLauncher.launchAllNotes()
        },
        NoteAdapterItem(requireContext()) {
            analytics.sendClick("EditNote")
            dependencies.notesFeatureLauncher.launchNoteEdit(
                note = it,
                targetFragment = parentFragment,
                requestCode = REQUEST_CODE_UPDATE_DATA
            )
        },
        AddActionAdapterItem(),
        DayStatusAdapterItem(),
        DashboardClassesAdapterItem(requireContext()) {
            analytics.sendClick("Classes")
            feature.accept(Wish.Click.OnClasses(it))
        },
        EventsHeaderAdapterItem {
            analytics.sendClick("ChangeGroup")
            dependencies.scheduleFeatureLauncher.launchSearchGroup(
                continueTo = CONTINUE_TO_BACK_STACK_WITH_RESULT,
                targetFragment = parentFragment,
                requestCode = REQUEST_CODE_UPDATE_DATA
            )
        },
        EmptyStateAdapterItem(),
        FavoriteScheduleAdapterItem {
            analytics.sendClick("FavoriteSchedule")
            feature.accept(Wish.Action.SelectFavoriteSchedule(it.value))
        },
        BannerAdapterItem(
            onClickListener = {
                closeFeatureBanner()
                dependencies.bottomTabsSwitcher.changeTab(BottomTab.PROFILE)
                dependencies.appSettingsFeatureLauncher.launch(AppSettingsFeatureLauncher.SubPage.FAVORITES)
            },
            onCloseListener = {
                closeFeatureBanner()
            }
        )
    )

    private fun closeFeatureBanner() {
        feature.accept(Wish.Action.CloseFeatureBanner)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_UPDATE_DATA) {
            feature.accept(Wish.Action.OnSwipeRefresh)
        }
    }

    override fun onUpdate() {
        feature.accept(Wish.Action.SilentUpdate)
    }
}