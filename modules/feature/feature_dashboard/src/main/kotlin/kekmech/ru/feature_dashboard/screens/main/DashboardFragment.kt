package kekmech.ru.feature_dashboard.screens.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.ActivityResultListener
import kekmech.ru.common_android.EmptyResult
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.openLinkExternal
import kekmech.ru.common_android.setResultListener
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.views.setProgressViewOffset
import kekmech.ru.common_elm.BaseFragment
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.features.ScrollToTop
import kekmech.ru.common_navigation.features.TabScreenStateSaver
import kekmech.ru.common_navigation.features.TabScreenStateSaverImpl
import kekmech.ru.common_schedule.items.NotePreviewAdapterItem
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.AddActionAdapterItem
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.ErrorStateAdapterItem
import kekmech.ru.coreui.items.FavoriteScheduleAdapterItem
import kekmech.ru.coreui.items.NoteAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.TextAdapterItem
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.feature_dashboard.R
import kekmech.ru.feature_dashboard.databinding.FragmentDashboardBinding
import kekmech.ru.feature_dashboard.di.DashboardDependencies
import kekmech.ru.feature_dashboard.items.BannerLunchAdapterItem
import kekmech.ru.feature_dashboard.items.BannerOpenSourceAdapterItem
import kekmech.ru.feature_dashboard.items.DashboardClassesAdapterItem
import kekmech.ru.feature_dashboard.items.DayStatusAdapterItem
import kekmech.ru.feature_dashboard.items.ScheduleTypeAdapterItem
import kekmech.ru.feature_dashboard.items.SearchFieldAdapterItem
import kekmech.ru.feature_dashboard.items.SessionAdapterItem
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEffect
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState
import kekmech.ru.strings.Strings
import org.koin.android.ext.android.inject
import vivid.money.elmslie.storepersisting.retainInParentStoreHolder

internal class DashboardFragment :
    BaseFragment<DashboardEvent, DashboardEffect, DashboardState>(),
    ActivityResultListener,
    ScrollToTop,
    TabScreenStateSaver by TabScreenStateSaverImpl("dashboard") {

    override val initEvent = DashboardEvent.Ui.Init
    override var layoutId = R.layout.fragment_dashboard
    override val storeHolder by retainInParentStoreHolder(storeProvider = ::createStore)

    private val dependencies by inject<DashboardDependencies>()
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
            recyclerView.addScrollAnalytics(analytics, "DashboardRecyclerView")
            swipeRefresh.apply {
                setOnRefreshListener { feature.accept(DashboardEvent.Ui.Action.SwipeToRefresh) }
                doOnApplyWindowInsets { _, windowInsets, _ ->
                    setProgressViewOffset(windowInsets.systemWindowInsetTop)
                }
            }
            restoreState(recyclerView)
        }
    }

    override fun onDestroyView() {
        saveState(viewBinding.recyclerView)
        super.onDestroyView()
    }

    override fun render(state: DashboardState) {
        adapter.update(DashboardListConverter(requireContext()).map(state))
        viewBinding.swipeRefresh.post {
            viewBinding.swipeRefresh.isRefreshing = state.isLoading
        }
    }

    override fun handleEffect(effect: DashboardEffect) = when (effect) {
        is DashboardEffect.ShowLoadingError -> showBanner(Strings.dashboard_loading_error)
        is DashboardEffect.ShowNotesLoadingError -> showBanner(Strings.something_went_wrong_error)
        is DashboardEffect.NavigateToNotesList -> {
            parentFragment?.setResultListener<EmptyResult>(NOTES_LIST_RESULT_KEY) {
                feature.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
            }
            dependencies.notesFeatureLauncher.launchNoteList(
                selectedClasses = effect.classes,
                selectedDate = effect.date,
                resultKey = NOTES_LIST_RESULT_KEY,
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
            dependencies.appSettingsFeatureLauncher.launch(AppSettingsFeatureLauncher.SubPage.FAVORITES)
        },
        NoteAdapterItem(requireContext()) {
            analytics.sendClick("EditNote")
            parentFragment?.setResultListener<EmptyResult>(EDIT_NOTE_RESULT_KEY) {
                feature.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
            }
            dependencies.notesFeatureLauncher.launchNoteEdit(
                note = it,
                resultKey = EDIT_NOTE_RESULT_KEY
            )
        },
        AddActionAdapterItem(),
        DayStatusAdapterItem(),
        DashboardClassesAdapterItem(requireContext(), ::clickOnClasses),
        ScheduleTypeAdapterItem {
            analytics.sendClick("ChangeGroup")
            parentFragment?.setResultListener<String>(FIND_GROUP_RESULT_KEY) {
                feature.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
            }
            dependencies.scheduleFeatureLauncher.launchSearchGroup(
                continueTo = ScheduleFeatureLauncher.ContinueTo.BACK_WITH_RESULT,
                resultKey = FIND_GROUP_RESULT_KEY
            )
        },
        EmptyStateAdapterItem(),
        FavoriteScheduleAdapterItem {
            analytics.sendClick("FavoriteSchedule")
            feature.accept(DashboardEvent.Ui.Action.SelectFavoriteSchedule(it.value))
        },
        NotePreviewAdapterItem(::clickOnClasses, R.layout.item_note_preview_padding_horisontal_8dp),
        TextAdapterItem(R.layout.item_time_prediction),
        SessionAdapterItem(requireContext()),
        ShimmerAdapterItem(R.layout.item_events_shimmer),
        ErrorStateAdapterItem {
            analytics.sendClick("DashboardReload")
            feature.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
        }
    )

    private fun clickOnClasses(it: Classes) {
        analytics.sendClick("ClickClasses")
        feature.accept(DashboardEvent.Ui.Click.ClassesItem(it))
    }

    override fun onScrollToTop() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            viewBinding.recyclerView.scrollToPosition(0)
        }
    }

    companion object {

        private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
        private const val EDIT_NOTE_RESULT_KEY = "EDIT_NOTE_RESULT_KEY"
        private const val FIND_GROUP_RESULT_KEY = "FIND_GROUP_RESULT_KEY"

        internal const val SECTION_NOTES_ACTION = 1
        internal const val SECTION_FAVORITES_ACTION = 2
    }
}
