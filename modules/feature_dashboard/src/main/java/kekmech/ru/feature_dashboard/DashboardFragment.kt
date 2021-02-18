package kekmech.ru.feature_dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.views.setProgressViewOffset
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.NeedToUpdate
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher.SubPage.FAVORITES
import kekmech.ru.domain_schedule.CONTINUE_TO_BACK_STACK_WITH_RESULT
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_dashboard.databinding.FragmentDashboardBinding
import kekmech.ru.feature_dashboard.di.DashboardDependencies
import kekmech.ru.feature_dashboard.items.*
import kekmech.ru.feature_dashboard.elm.DashboardEffect
import kekmech.ru.feature_dashboard.elm.DashboardEvent
import kekmech.ru.feature_dashboard.elm.DashboardEvent.Wish
import kekmech.ru.feature_dashboard.elm.DashboardState
import org.koin.android.ext.android.inject
import vivid.money.elmslie.core.store.Store

private const val REQUEST_CODE_UPDATE_DATA = 2910
internal const val SECTION_NOTES_ACTION = 1
internal const val SECTION_FAVORITES_ACTION = 2

class DashboardFragment :
    BaseFragment<DashboardEvent, DashboardEffect, DashboardState>(),
    ActivityResultListener,
    NeedToUpdate {

    override val initEvent = Wish.Init
    private val dependencies by inject<DashboardDependencies>()

    override var layoutId = R.layout.fragment_dashboard
    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("Dashboard")
    private val viewBinding by viewBinding(FragmentDashboardBinding::bind)

    override fun createStore() = dependencies.dashboardFeatureFactory.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
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
        }
    }

    override fun render(state: DashboardState) {
        adapter.update(DashboardListConverter(requireContext()).map(state))
        viewBinding.swipeRefresh.post {
            viewBinding.swipeRefresh.isRefreshing = state.isSwipeRefreshLoadingAnimation
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

    @Suppress("LongMethod")
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
        SectionHeaderAdapterItem(),
        SectionHeaderAdapterItem(SECTION_NOTES_ACTION) {
            analytics.sendClick("ShowAllNotes")
            dependencies.notesFeatureLauncher.launchAllNotes()
        },
        SectionHeaderAdapterItem(SECTION_FAVORITES_ACTION) {
            analytics.sendClick("SetUpFavorites")
            dependencies.appSettingsFeatureLauncher.launch(FAVORITES)
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
        DashboardClassesAdapterItem(requireContext(), ::clickOnClasses),
        ScheduleTypeAdapterItem {
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
                analytics.sendClick("DashboardFeatureBannerOpen")
                closeFeatureBanner()
                dependencies.bottomTabsSwitcher.changeTab(BottomTab.PROFILE)
                dependencies.appSettingsFeatureLauncher.launch(FAVORITES)
            },
            onCloseListener = {
                analytics.sendClick("DashboardFeatureBannerClose")
                closeFeatureBanner()
            }
        ),
        NotePreviewAdapterItem(::clickOnClasses, R.layout.item_note_preview_padding_horisontal_8dp),
        TextAdapterItem(R.layout.item_time_prediction),
        SessionAdapterItem(requireContext())
    )

    private fun clickOnClasses(it: Classes) {
        analytics.sendClick("ClickClasses")
        feature.accept(Wish.Click.OnClasses(it))
    }

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