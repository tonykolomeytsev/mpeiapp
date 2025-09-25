package kekmech.ru.feature_search_impl.screens.schedule_details

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.TextAdapterItem
import kekmech.ru.coreui.items.TextWithIconAdapterItem
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.fragment.BottomSheetDialogFragment
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_search_impl.R
import kekmech.ru.feature_search_impl.databinding.FragmentScheduleDetailsBinding
import kekmech.ru.feature_search_impl.item.ButtonAdapterItem
import kekmech.ru.feature_search_impl.item.WeekMinItem
import kekmech.ru.feature_search_impl.item.WeekMinItemBinder
import kekmech.ru.feature_search_impl.item.WeekMinViewHolder
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEffect
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Ui
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsStoreFactory
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.ClearBackStack
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_schedule.items.ClassesItemBinder
import kekmech.ru.lib_schedule.items.ClassesViewHolderImpl
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject
import kekmech.ru.coreui.R as coreui_R
import kekmech.ru.lib_schedule.R as common_schedule_R

private const val ARG_RESULT_ITEM = "Arg.Item"
internal const val ITEM_BUTTON_SWITCH = 1
internal const val ITEM_FAVORITES = 1

internal class ScheduleDetailsFragment :
    BottomSheetDialogFragment(R.layout.fragment_schedule_details),
    ElmRendererDelegate<ScheduleDetailsEffect, ScheduleDetailsState> {

    private val viewBinding by viewBinding(FragmentScheduleDetailsBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val router by inject<Router>()
    private val bottomTabsSwitcher by inject<BottomTabsSwitcher>()
    private val analytics by screenAnalytics("SearchScheduleDetails")

    private val store by androidElmStore {
        inject<ScheduleDetailsStoreFactory>().value.create(
            searchResult = notNullSerializableArg(ARG_RESULT_ITEM)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        setFixedSizeWorkaround()
    }

    @Suppress("DEPRECATION") // currentWindowMetrics requires minApi = 30
    private fun setFixedSizeWorkaround() {
        val display = requireActivity().windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        viewBinding.recyclerView.minimumHeight = point.y
    }

    override fun render(state: ScheduleDetailsState) {
        adapter.update(ScheduleDetailsListConverter.map(state, requireContext()))
    }

    override fun handleEffect(effect: ScheduleDetailsEffect) = when (effect) {
        is ScheduleDetailsEffect.CloseAndGoToSchedule -> {
            close()
            router.executeCommand(ClearBackStack())
            bottomTabsSwitcher.changeTab(BottomTab.DASHBOARD)
        }
    }

    private fun createAdapter() = BaseAdapter(
        TextAdapterItem(),
        SpaceAdapterItem(),
        PullAdapterItem(),
        ShimmerAdapterItem(coreui_R.layout.item_text_shimmer),
        ShimmerAdapterItem(R.layout.item_week_shimmer),
        ShimmerAdapterItem(R.layout.item_classes_shimmer),
        ButtonAdapterItem(ITEM_BUTTON_SWITCH, R.layout.item_button) {
            analytics.sendClick("SwitchSchedule")
            store.accept(Ui.Click.SwitchSchedule)
        },
        TextWithIconAdapterItem {
            when (it.itemId) {
                ITEM_FAVORITES -> {
                    analytics.sendClick("AddToFavorites")
                    store.accept(Ui.Click.Favorites)
                }

                else -> Unit
            }
        },
        AdapterItem(
            isType = { it is WeekMinItem },
            layoutRes = R.layout.item_week_min,
            viewHolderGenerator = ::WeekMinViewHolder,
            itemBinder = WeekMinItemBinder(requireContext()) {
                store.accept(Ui.Click.Day(it.date))
            },
            areItemsTheSame = { _, _ -> true }
        ),
        AdapterItem(
            isType = { it is Classes },
            layoutRes = common_schedule_R.layout.item_classes_padding_horisontal_16dp,
            viewHolderGenerator = ::ClassesViewHolderImpl,
            itemBinder = ClassesItemBinder(requireContext()) { /* no-op */ }
        ),
        EmptyStateAdapterItem()
    )

    companion object {

        fun newInstance(resultItem: SearchResult) = ScheduleDetailsFragment()
            .withArguments(ARG_RESULT_ITEM to resultItem)
    }
}
