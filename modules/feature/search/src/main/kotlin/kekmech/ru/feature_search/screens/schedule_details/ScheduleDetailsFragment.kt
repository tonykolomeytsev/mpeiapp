package kekmech.ru.feature_search.screens.schedule_details

import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.TextAdapterItem
import kekmech.ru.coreui.items.TextWithIconAdapterItem
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.getArgument
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_search.R
import kekmech.ru.feature_search.databinding.FragmentScheduleDetailsBinding
import kekmech.ru.feature_search.item.ButtonAdapterItem
import kekmech.ru.feature_search.item.WeekMinItem
import kekmech.ru.feature_search.item.WeekMinItemBinder
import kekmech.ru.feature_search.item.WeekMinViewHolder
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEffect
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsEvent.Ui
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsState
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsStoreFactory
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseAdapter
import kekmech.ru.library_elm.BaseBottomSheetDialogFragment
import kekmech.ru.library_navigation.BottomTab
import kekmech.ru.library_navigation.BottomTabsSwitcher
import kekmech.ru.library_navigation.ClearBackStack
import kekmech.ru.library_navigation.Router
import kekmech.ru.library_schedule.items.ClassesItemBinder
import kekmech.ru.library_schedule.items.ClassesViewHolderImpl
import org.koin.android.ext.android.inject
import kekmech.ru.coreui.R as coreui_R
import kekmech.ru.library_schedule.R as common_schedule_R

private const val ARG_RESULT_ITEM = "Arg.Item"
internal const val ITEM_BUTTON_SWITCH = 1
internal const val ITEM_FAVORITES = 1

internal class ScheduleDetailsFragment :
    BaseBottomSheetDialogFragment<ScheduleDetailsEvent, ScheduleDetailsEffect,
            ScheduleDetailsState>(R.layout.fragment_schedule_details) {

    private val viewBinding by viewBinding(FragmentScheduleDetailsBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val router by inject<Router>()
    private val bottomTabsSwitcher by inject<BottomTabsSwitcher>()
    private val analytics by screenAnalytics("SearchScheduleDetails")

    override fun createStore() = inject<ScheduleDetailsStoreFactory>().value
        .create(getArgument(ARG_RESULT_ITEM))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        setFixedSizeWorkaround()
    }

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
