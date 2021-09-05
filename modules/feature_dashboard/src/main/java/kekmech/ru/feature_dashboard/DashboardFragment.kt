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
import kekmech.ru.common_schedule.items.NotePreviewAdapterItem
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher.SubPage.FAVORITES
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher.ContinueTo.BACK_WITH_RESULT
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_dashboard.databinding.FragmentDashboardBinding
import kekmech.ru.feature_dashboard.di.DashboardDependencies
import kekmech.ru.feature_dashboard.elm.DashboardEffect
import kekmech.ru.feature_dashboard.elm.DashboardEvent
import kekmech.ru.feature_dashboard.elm.DashboardEvent.Wish
import kekmech.ru.feature_dashboard.elm.DashboardState
import kekmech.ru.feature_dashboard.items.*
import org.koin.android.ext.android.inject

private const val REQUEST_CODE_UPDATE_DATA = 2910
internal const val SECTION_NOTES_ACTION = 1
internal const val SECTION_FAVORITES_ACTION = 2
internal const val EVENTS_SHIMMER_ITEM_ID = 0

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
                setOnRefreshListener { feature.accept(Wish.Action.SwipeToRefresh) }
                doOnApplyWindowInsets { _, windowInsets, _ ->
                    setProgressViewOffset(windowInsets.systemWindowInsetTop)
                }
            }
        }
    }

    override fun render(state: DashboardState) {
        adapter.update(DashboardListConverter(requireContext()).map(state))
        viewBinding.swipeRefresh.post {
            viewBinding.swipeRefresh.isRefreshing = state.isLoading && state.currentWeekSchedule == null
        }
    }

    override fun handleEffect(effect: DashboardEffect) = when (effect) {
        is DashboardEffect.ShowLoadingError -> showBanner(R.string.dashboard_loading_error)
        is DashboardEffect.ShowNotesLoadingError -> showBanner(R.string.something_went_wrong_error)
        is DashboardEffect.NavigateToNotesList -> {
            dependencies.notesFeatureLauncher.launchNoteList(
                selectedClasses = effect.classes,
                selectedDate = effect.date,
                resultKey = NOTES_LIST_RESULT_KEY,
            )
            setResultListener<EmptyResult>(NOTES_LIST_RESULT_KEY) {
                feature.accept(Wish.Action.SwipeToRefresh)
            }
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
                resultKey = EDIT_NOTE_RESULT_KEY
            )
            setResultListener<EmptyResult>(EDIT_NOTE_RESULT_KEY) {
                feature.accept(Wish.Action.SwipeToRefresh)
            }
        },
        AddActionAdapterItem(),
        DayStatusAdapterItem(),
        DashboardClassesAdapterItem(requireContext(), ::clickOnClasses),
        ScheduleTypeAdapterItem {
            analytics.sendClick("ChangeGroup")
            dependencies.scheduleFeatureLauncher.launchSearchGroup(
                continueTo = BACK_WITH_RESULT,
                resultKey = FIND_GROUP_RESULT_KEY
            )
            setResultListener<String>(FIND_GROUP_RESULT_KEY) { _ ->
                feature.accept(Wish.Action.SwipeToRefresh)
            }
        },
        EmptyStateAdapterItem(),
        FavoriteScheduleAdapterItem {
            analytics.sendClick("FavoriteSchedule")
            feature.accept(Wish.Action.SelectFavoriteSchedule(it.value))
        },
        NotePreviewAdapterItem(::clickOnClasses, R.layout.item_note_preview_padding_horisontal_8dp),
        TextAdapterItem(R.layout.item_time_prediction),
        SessionAdapterItem(requireContext()),
        ShimmerAdapterItem(EVENTS_SHIMMER_ITEM_ID, R.layout.item_events_shimmer)
    )

    private fun clickOnClasses(it: Classes) {
        analytics.sendClick("ClickClasses")
        feature.accept(Wish.Click.OnClasses(it))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_UPDATE_DATA) {
            feature.accept(Wish.Action.SwipeToRefresh)
        }
    }

    override fun onUpdate() {
        feature.accept(Wish.Action.SilentUpdate)
    }

    companion object {

        private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
        private const val EDIT_NOTE_RESULT_KEY = "EDIT_NOTE_RESULT_KEY"
        private const val FIND_GROUP_RESULT_KEY = "FIND_GROUP_RESULT_KEY"
    }
}