package kekmech.ru.feature_dashboard_impl.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
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
import kekmech.ru.ext_android.ActivityResultListener
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.addSystemVerticalPadding
import kekmech.ru.ext_android.doOnApplyWindowInsets
import kekmech.ru.ext_android.notNullSerializable
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.views.setProgressViewOffset
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.feature_dashboard_impl.databinding.FragmentDashboardBinding
import kekmech.ru.feature_dashboard_impl.di.DashboardDependencies
import kekmech.ru.feature_dashboard_impl.presentation.items.BannerLunchAdapterItem
import kekmech.ru.feature_dashboard_impl.presentation.items.DashboardClassesAdapterItem
import kekmech.ru.feature_dashboard_impl.presentation.items.DayStatusAdapterItem
import kekmech.ru.feature_dashboard_impl.presentation.items.ScheduleTypeAdapterItem
import kekmech.ru.feature_dashboard_impl.presentation.items.SearchFieldAdapterItem
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEffect
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardStoreFactory
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.addScrollAnalytics
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.features.ScrollToTop
import kekmech.ru.lib_navigation.features.TabScreenStateSaver
import kekmech.ru.lib_navigation.features.TabScreenStateSaverImpl
import kekmech.ru.lib_schedule.items.NotePreviewAdapterItem
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject
import kekmech.ru.res_strings.R.string as Strings

internal class DashboardFragment :
    Fragment(R.layout.fragment_dashboard),
    ElmRendererDelegate<DashboardEffect, DashboardState>,
    ActivityResultListener,
    ScrollToTop,
    TabScreenStateSaver by TabScreenStateSaverImpl("dashboard") {

    private val dependencies by inject<DashboardDependencies>()
    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("Dashboard")
    private val viewBinding by viewBinding(FragmentDashboardBinding::bind)
    private val router by inject<Router>()
    private val store by androidElmStore(
        viewModelStoreOwner = { requireActivity() },
        savedStateRegistryOwner = { requireActivity() },
    ) { inject<DashboardStoreFactory>().value.create() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.addSystemVerticalPadding()
            recyclerView.addScrollAnalytics(analytics, "DashboardRecyclerView")
            swipeRefresh.apply {
                setOnRefreshListener { store.accept(DashboardEvent.Ui.Action.SwipeToRefresh) }
                doOnApplyWindowInsets { _, windowInsets, _ ->
                    val systemBarsInsets =
                        windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                    setProgressViewOffset(systemBarsInsets.top)
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
            parentFragment?.setResultListener<EmptyResult>(
                key = NOTES_LIST_RESULT_KEY,
                resultMapper = Bundle::notNullSerializable,
            ) {
                store.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
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
            parentFragment?.setResultListener<EmptyResult>(
                key = EDIT_NOTE_RESULT_KEY,
                resultMapper = Bundle::notNullSerializable,
            ) {
                store.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
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
            parentFragment?.setResultListener<SelectedSchedule>(
                key = FIND_GROUP_RESULT_KEY,
                resultMapper = Bundle::notNullSerializable,
            ) {
                store.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
            }
            router.executeCommand(AddScreenForward {
                dependencies.scheduleFeatureApi.getSearchGroupScreen(
                    continueTo = ScheduleFeatureApi.ContinueTo.BACK_WITH_RESULT,
                    resultKey = FIND_GROUP_RESULT_KEY
                )
            })
        },
        EmptyStateAdapterItem(),
        FavoriteScheduleAdapterItem {
            analytics.sendClick("FavoriteSchedule")
            store.accept(DashboardEvent.Ui.Action.SelectFavoriteSchedule(it.value))
        },
        NotePreviewAdapterItem(::clickOnClasses, R.layout.item_note_preview_padding_horisontal_8dp),
        TextAdapterItem(R.layout.item_time_prediction),
        ShimmerAdapterItem(R.layout.item_events_shimmer),
        ErrorStateAdapterItem {
            analytics.sendClick("DashboardReload")
            store.accept(DashboardEvent.Ui.Action.SwipeToRefresh)
        }
    )

    private fun clickOnClasses(it: Classes) {
        analytics.sendClick("ClickClasses")
        store.accept(DashboardEvent.Ui.Click.ClassesItem(it))
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
